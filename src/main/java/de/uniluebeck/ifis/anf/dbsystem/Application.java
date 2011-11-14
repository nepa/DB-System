package de.uniluebeck.ifis.anf.dbsystem;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;
import de.uniluebeck.ifis.anf.dbsystem.parser.gene.ParseException;
import de.uniluebeck.ifis.anf.dbsystem.parser.gene.SimpleSQLParser;
import de.uniluebeck.ifis.anf.dbsystem.parser.syntaxtree.CompilationUnit;
import de.uniluebeck.ifis.anf.dbsystem.parser.visitor.ObjectDepthFirst;
import java.io.StringReader;

/**
 * DB-System main application.
 */
public class Application
{
  /** Path to customer database */
  public static final String KUNDENDB = "";

  /**
   * Application program to run DB-System application.
   *
   * @param args Array of commandline arguments
   */
  public static void main(String[] args)
  {
    // TODO: Add tests here?
  }
  
  /**
   * Execute an SimpleSQL query on the database and print result.
   * 
   * @param query SimpleSQL query
   */
  public static void executeQuery(final String query)
  {
    // Translate SimpleSQL query to execution plan
    ITreeNode executionPlan = Application.sqlToRelationenAlgebra(query);
    
    // Execute execution plan
    Relation result = Application.executePlan(executionPlan);
    
    // Print resulting table
    Application.printTable(result.toTable());
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
      
      ObjectDepthFirst visitor = new ObjectDepthFirst();
      executionPlan = (ITreeNode)compUnit.accept(visitor, null); // TODO: Pass query as argu?
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    
    return executionPlan;
  }

  private static Relation executePlan(final ITreeNode executionPlan)
  {
    return executionPlan.evaluate();
  }
  
  /**
   * Private helper method to print Table object.
   */
  public static void printTable(final Table table)
  {
    // Print table object
    System.out.println("\n\n" + table + "\n\n");
  }
  
  /**
   * Private helper method to create KundenDB once.
   */
  public static void createKundenDB()
  {
    Table buchTable = new Table("Buch", "b", new String[] { "ID", "Titel" });
    
    Table kundenTable = new Table("Kunde", "k", new String[] { "ID", "Name", "Vorname", "Adresse" });
    
    //return table;
  }  
}
