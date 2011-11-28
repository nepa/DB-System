package de.uniluebeck.ifis.anf.dbsystem;

import java.io.File;
import java.util.Scanner;
import java.io.StringReader;
import java.io.FileNotFoundException;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;
import de.uniluebeck.ifis.anf.dbsystem.parser.gene.ParseException;
import de.uniluebeck.ifis.anf.dbsystem.parser.gene.SimpleSQLParser;
import de.uniluebeck.ifis.anf.dbsystem.parser.syntaxtree.CompilationUnit;
import de.uniluebeck.ifis.anf.dbsystem.algebra.SimpleSQLToRelAlgVisitor;

/**
 * DB-System main application.
 */
public class Application
{
  /** Path to SimpleSQL queries (with trailing slash!) */  

  public static final String QUERY_PATH = System.getProperty("user.home") + "/anfrage/repo/src/test/resources/";
//  public static final String QUERY_PATH = System.getProperty("user.home") + "/NetBeansProjects/DB-System/src/test/resources/";

  /**
   * Application program to run DB-System application.
   *
   * @param args Array of commandline arguments
   */
  public static void main(String[] args)
  {
    try
    {
      // Create KundenDB
      Application.createKundenDB();
      
      // Execute some queries on KundenDB
      Application.executeQueriesFromFile(QUERY_PATH + "sql.txt");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Execute a SimpleSQL query on the database and print result.
   * 
   * @param query SimpleSQL query
   * 
   * @return Resulting table object
   *
   * @throws Exception Error during query execution
   */
  public static Table executeQuery(final String query) throws Exception
  {
    // Translate SimpleSQL query to execution plan
    ITreeNode executionPlan = Application.sqlToRelationenAlgebra(query);

    // Execute execution plan
    Relation result = Application.executePlan(executionPlan);

    // Do some debug output
    System.out.println("=================================================================\n");
    System.out.println("Query: " + query);
    System.out.println("Costs: " + executionPlan.getCosts());
    Application.printTable(result.toTable());

    return result.toTable();
  }

  /**
   * Load multiple SimpleSQL queries from a file and execute them.
   *
   * @param pathToFile Fully qualified path to the input file
   *
   * @return Number of executed queries
   *
   * @throws Exception Error during query execution
   */
  public static int executeQueriesFromFile(final String pathToFile) throws Exception
  {
    int queryCounter = 0;
    Scanner inputFile = null;

    // Try to load data from file
    try
    {
      inputFile = new Scanner(new File(pathToFile));
    }
    catch (FileNotFoundException e)
    {
      String fileName = (new File(pathToFile).getName());
      System.err.println(String.format("Could not load queries from file '%s'. It does not exist.", fileName));
      System.exit(1);
    }

    // Process all queries by reading file line by line
    while (null != inputFile && inputFile.hasNext())
    {
      // Get next line
      String line = inputFile.nextLine();

      // Check if line ends with semicolon
      if (line.length() > 0 && (";").equals(String.valueOf(line.charAt(line.length() - 1))))
      {
        // Query is line without semicolon
        String query = line.substring(0, line.length() -1);

        // Execute query
        Application.executeQuery(query);
        queryCounter++;
      }
    }

    return queryCounter;
  }

  /**
   * Translate a SimpleSQL query to an ITreeNode object that
   * contains an execution plan.
   * 
   * @param query SimpleSQL query
   * 
   * @return Execution plan as ITreeNode object
   */
  public static ITreeNode sqlToRelationenAlgebra(final String query)
  {
    ITreeNode executionPlan = null;

    // Parse SimpleSQL query
    SimpleSQLParser parser = new SimpleSQLParser(new StringReader(query));
    parser.setDebugALL(false);

    try
    {
      CompilationUnit compUnit = parser.CompilationUnit();
      SimpleSQLToRelAlgVisitor visitor = new SimpleSQLToRelAlgVisitor();
      compUnit.accept(visitor, null);
      executionPlan = visitor.getExecutionPlan();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }

    return executionPlan;
  }

  /**
   * Evaluate an execution plan and return Relation object as result.
   *
   * @param executionPlan Execution plan for evaluation
   *
   * @return Resulting Relation object
   *
   * @throws Exception Error during execution
   */
  private static Relation executePlan(final ITreeNode executionPlan) throws Exception
  {
    return executionPlan.evaluate();
  }

  /**
   * Private helper method to print Table object.
   */
  public static void printTable(final Table table)
  {
    System.out.println("\n\n" + table + "\n\n");
  }

  /**
   * Private helper method to initially create and fill KundenDB.
   *
   * @throws Exception Error during query execution
   */
  public static void createKundenDB() throws Exception
  {
    Application.executeQueriesFromFile(QUERY_PATH + "kundendb.txt");
  }
}
