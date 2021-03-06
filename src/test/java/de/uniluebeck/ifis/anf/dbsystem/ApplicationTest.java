package de.uniluebeck.ifis.anf.dbsystem;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.*;
import de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.*;
import de.uniluebeck.ifis.anf.dbsystem.optimierung.CascadeSelects;
import de.uniluebeck.ifis.anf.dbsystem.optimierung.DetectJoins;
import de.uniluebeck.ifis.anf.dbsystem.optimierung.MoveProjection;
import de.uniluebeck.ifis.anf.dbsystem.optimierung.MoveSelection;
import de.uniluebeck.ifis.anf.dbsystem.transaction.ClientThread;

/**
 * Unit test for DB-System application.
 */
public class ApplicationTest
{
  /**
   * Test output of table data to System.out.
   */
  @Test(timeout = 1000)
  public void testTableOutput()
  {
    this.printCaption("Testing table output...");

    Table table = this.createTestTable();

    this.printTable(table);
    
    // Test if output is not empty and contains data
    assertTrue("Output of table must not be empty.", !("").equals(table.toString()));
    assertTrue("Output must contain data of table.", table.toString().contains("Bob"));
  }

  /**
   * Test writing and loading of Table objects
   */
  @Test(timeout = 1000)
  public void testSerialization() throws Exception
  {
    this.printCaption("Testing table serialization...");

    System.out.println("Serializing table...");
    this.testTableWrite();

    System.out.print("Deserializing table...");
    this.testTableLoad();
  }

  /**
   * Test table operations.
   */
  @Test(timeout = 10000)
  public void testTableOperations() throws Exception
  {
    this.printCaption("Test table operations...");

    String tableName = "Person";
    
    // Test CREATE TABLE operation
    CreateTable createTableOperation = new CreateTable();
    createTableOperation.setName(tableName);
    createTableOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });

    System.out.println(String.format("Creating table '%s'...", tableName));
    Table table = this.executePlan(createTableOperation);
    assertNotNull("Table object must not be null.", table);
    this.printTable(table);
    
    // Test INSERT operation
    Insert insertOperation = new Insert();
    insertOperation.setName(tableName);
    insertOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });
    insertOperation.setValues(new String[] { "Max", "Mustermann", "42" });
    
    System.out.println("Inserting entry 'Max'...");
    table = this.executePlan(insertOperation);
    assertTrue("Table must contain entry that was inserted.", table.toString().contains("Max"));
    this.printTable(table);
    
    // Test another INSERT operation
    insertOperation = new Insert();
    insertOperation.setName(tableName);
    insertOperation.setColumnNames(new String[] { "Firstname", "Age" });
    insertOperation.setValues(new String[] { "Vanessa", "27" });

    System.out.println("Inserting entry 'Vanessa'...");
    table = this.executePlan(insertOperation);
    assertTrue("Table must contain entry that was inserted.", table.toString().contains("Vanessa"));
    this.printTable(table);
    
    // Test UPDATE operation
    EqualityExpression equalityExpression = new EqualityExpression();
    equalityExpression.setFirstExpression(new PrimaryExpression("Vanessa", true));
    equalityExpression.setSecondExpression(new PrimaryExpression("Firstname", false));
    equalityExpression.setOperator("=");
    
    AndExpression andExpression = new AndExpression(equalityExpression);
        
    Update updateOperation = new Update();
    updateOperation.setName(tableName);
    updateOperation.setWhereClause(andExpression);
    updateOperation.setColumnNames(new String[] { "Lastname" });
    updateOperation.setValues(new String[] { "Meier" });

    System.out.println("Updating entry 'Vanessa'...");
    table = this.executePlan(updateOperation);
    assertTrue("Table must contain altered entry.", table.toString().contains("Meier"));
    this.printTable(table);
    
    // Test DELETE operation
    equalityExpression = new EqualityExpression();
    equalityExpression.setFirstExpression(new PrimaryExpression("Mustermann", true));
    equalityExpression.setSecondExpression(new PrimaryExpression("Lastname", false));
    equalityExpression.setOperator("=");
    
    andExpression = new AndExpression(equalityExpression);
    
    Delete deleteOperation = new Delete();
    deleteOperation.setName(tableName);
    deleteOperation.setWhereClause(andExpression);

    System.out.println("Deleting entry 'Max'...");
    table = this.executePlan(deleteOperation);
    assertTrue("Table must not contain deleted entry anymore.", !table.toString().contains("Mustermann"));
    this.printTable(table);
    
    // Test DROP TABLE operation
    DropTable dropTableOperation = new DropTable();
    dropTableOperation.setName(tableName);
    
    System.out.println(String.format("Dropping table '%s'...", tableName));
    table = this.executePlan(dropTableOperation);
    assertTrue("Drop flag must be set for table.", table.isDropped());
    this.printTable(table);
  }
  
  /**
   * Test execution of SQL queries.
   */
  @Test(timeout = 50000)
  public void testSQLQueries() throws Exception
  {
    this.printCaption("Testing SimpleSQL queries...");

    Application.createKundenDB();

    // Test SELECT statement
    Table table = Application.executeQuery("select Titel from Buch where ID = \"Buch2\"");
    assertTrue("Selection must return one result.", table.getRows().size() == 1);
    assertTrue("Result must contain desired author name.", table.getRow(0)[0].equals("Kennedys Hirn"));
    
    // Test SELECT statement without where clause
    table = Application.executeQuery("select ID, Titel from Buch");
    assertTrue("Selection must return ten results.", table.getRows().size() == 10);
    
    // Test INSERT statement
    table = Application.executeQuery("insert into Buch(ID, Titel) values (\"MeinBuch\", \"Titel von meinem Buch\")");
    table = Application.executeQuery("select ID, Titel from Buch where ID = \"MeinBuch\"");
    assertTrue("Selection must return one result.", table.getRows().size() == 1);
    assertTrue("Result must contain desired book title.", table.getRow(0)[1].equals("Titel von meinem Buch"));

    // Test UPDATE statement
    table = Application.executeQuery("update Kunde set Vorname = \"Max\", Name = \"Mustermann\" where ID = \"Kunde1\"");
    table = Application.executeQuery("select Name from Kunde where ID = \"Kunde1\"");
    assertTrue("Selection must return one result.", table.getRows().size() == 1);
    assertTrue("Result must contain desired customer name.", table.getRow(0)[0].equals("Mustermann"));

    table = Application.executeQuery("update Kunde set Vorname = \"Jan\", Name = \"Mustermann\" where ID = \"Kunde2\"");
    table = Application.executeQuery("select Name, Vorname from Kunde where ID = \"Kunde1\" or ID = \"Kunde2\"");
    assertTrue("Selection must return one result.", table.getRows().size() == 2);
    assertTrue("Result must contain desired customer name.", table.getRow(0)[1].equals("Max"));
    assertTrue("Result must contain desired customer name.", table.getRow(1)[1].equals("Jan"));

    table = Application.executeQuery("update Kunde set Name = \"Huber\" where Name = \"Mustermann\"");
    table = Application.executeQuery("select Name from Kunde where Name = \"Mustermann\"");
    assertTrue("Selection must return one result.", table.getRows().isEmpty());
    table = Application.executeQuery("select Name, Vorname from Kunde where Name = \"Huber\"");
    assertTrue("Selection must return one result.", table.getRows().size() == 2);
    assertTrue("Result must contain desired customer name.", table.getRow(0)[1].equals("Max"));
    assertTrue("Result must contain desired customer name.", table.getRow(1)[1].equals("Jan"));

    // Test DELETE statement
    table = Application.executeQuery("delete from Kunde where Name = \"Huber\"");
    table = Application.executeQuery("select Name from Kunde where Name = \"Huber\"");
    assertTrue("Selection must not return any result.", table.getRows().isEmpty());

    // Test CREATE TABLE statement
    table = Application.executeQuery("create table TemporaryTable(ID varchar, column1 varchar, column2 varchar)");
    table = Application.executeQuery("insert into TemporaryTable(ID, column1, column2) values(\"1\", \"Foo\", \"Bar\")");
    table = Application.executeQuery("select ID, column1, column2 from TemporaryTable where ID = \"1\"");
    assertTrue("Selection must return one result.", table.getRows().size() == 1);

    // Test DROP TABLE statement
    table = Application.executeQuery("drop TemporaryTable");
    boolean tableWasDropped = false;
    try
    {
      table = Application.executeQuery("drop TemporaryTable");
    }
    // Should throw exception, because table does not exist anymore
    catch (NullPointerException e)
    {
      tableWasDropped = true;
    }
    assertTrue("Table must have been dropped.", tableWasDropped);
  }

  /**
   * Test various query optimizations.
   */
  @Test(timeout = 10000)
  public void testOptimizedQuery() throws Exception
  {
    this.printCaption("Testing query optimizations...");

    // Test cascaded selects
    ITreeNode executionPlan = createRelAlgTree();
    ITreeNode optimizedCascadePlan = new CascadeSelects().optimize(createRelAlgTree());
    assertEquals(executionPlan.evaluate().toTable().toString(), optimizedCascadePlan.evaluate().toTable().toString());

    this.printTable(optimizedCascadePlan.evaluate().toTable());

    Projection projection = (Projection) optimizedCascadePlan;
    Selection selection = (Selection)projection.getChild();

    assertTrue(selection.getChild().getClass() == Selection.class);
    
    // Test moved selections
    ITreeNode optimizedMoveSelectionPlan = new MoveSelection().optimize(new CascadeSelects().optimize(createRelAlgTree()));
    
    assertEquals(executionPlan.evaluate().toTable().toString(), optimizedMoveSelectionPlan.evaluate().toTable().toString());
    assertTrue(optimizedMoveSelectionPlan.getCosts() < optimizedCascadePlan.getCosts());
    
    // Test replacement with join operation
    ITreeNode optimizedJoinPlan = new DetectJoins().optimize(new MoveSelection().optimize(new CascadeSelects().optimize(createRelAlgTree())));
    projection = (Projection) optimizedJoinPlan;
    
    assertTrue(projection.getChild().getClass() == Join.class);
    assertEquals(executionPlan.evaluate().toTable().toString(), optimizedJoinPlan.evaluate().toTable().toString());
    assertTrue(optimizedJoinPlan.getCosts() < optimizedMoveSelectionPlan.getCosts());
    
    // Test moved projections
    ITreeNode optimizedMoveProjectionPlan = new MoveProjection().optimize(new DetectJoins().optimize(new MoveSelection().optimize(new CascadeSelects().optimize(createRelAlgTree()))));
    assertTrue(optimizedMoveProjectionPlan.getClass() == Join.class);
    assertEquals(executionPlan.evaluate().toTable().toString(), optimizedMoveProjectionPlan.evaluate().toTable().toString());
    assertTrue(optimizedMoveProjectionPlan.getCosts() < optimizedJoinPlan.getCosts());
    
    this.printCaption("Optimization Costs");
    System.out.println("Costs normal:     " + executionPlan.getCosts());
    System.out.println("Costs cascade:    " + optimizedCascadePlan.getCosts());
    System.out.println("Costs + moveSel:  " + optimizedMoveSelectionPlan.getCosts());
    System.out.println("Costs + join:     " + optimizedJoinPlan.getCosts());
    System.out.println("Costs + moveProj: " + optimizedMoveProjectionPlan.getCosts());
  }

  /**
   * Test queries from block 2 assignment.
   */
  @Test(timeout = 10000)
  public void testBlock2() throws Exception
  {
    Application.createKundenDB();
    
    // Define queries
    String query1 = "select B.Titel from Buch as B, Kunde as K, Buch_Bestellung as BB, Kunde_Bestellung as KB where K.Name=\"KName1\" and K.ID=KB.K_ID and KB.B_ID=BB.Be_ID and BB.Bu_ID=B.ID";
    String query2 = "select B.ID, K.Name from Bestellung as B, Kunde as K, Kunde_Bestellung as KB where KB.K_ID=K.ID and KB.B_ID=B.ID and B.ID=\"Bestellung5\"";
    String query3 = "select Name from Kunde,Kunde_Bestellung where ID=K_ID and Name=\"KName1\"";
    
    for (String query: new String[] { query1, query2, query3 })
    {
      System.out.println("Query: " + query);
      
      ITreeNode executionPlan = Application.sqlToRelationenAlgebra(query);
      printTable(executionPlan.evaluate().toTable());
      
      System.out.println("Costs normal:               " + executionPlan.getCosts());
      
      executionPlan = new MoveSelection().optimize(new CascadeSelects().optimize(executionPlan));
      System.out.println("Costs inclusive exercise 2: " + executionPlan.getCosts());
      
      executionPlan = new DetectJoins().optimize(executionPlan);
      System.out.println("Costs inclusive exercise 3: " + executionPlan.getCosts());
      
      executionPlan = new MoveProjection().optimize(executionPlan);
      System.out.println("Costs inclusive exercise 4: " + executionPlan.getCosts());
    }
  }
  
  /**
   * Test transaction scheduler.
   */
  @Test(timeout = 10000)
  public void testTransactionScheduler() throws Exception
  {    
    Application.createKundenDB();
    
    ClientThread thread1 = new ClientThread("sql1.txt");
    ClientThread thread2 = new ClientThread("sql2.txt");
    ClientThread thread3 = new ClientThread("sql3.txt");
    
    thread1.start();
    thread2.start();
    thread3.start();
  }
  
  /**
   * Private helper method to test serialization of Table objects.
   */
  private void testTableWrite() throws Exception
  {
    Table table = this.createTestTable();

    System.out.println("Adding new row...");
    table.addRow(new String[] { "Serialize", "0", "Test", "0" });

    table.write();

    String fileName = Table.DATABASE_PATH + System.getProperty("file.separator")
            + table.getName() + Table.DATABASE_TABLE_FILE_EXTENSION;
    boolean fileExists = (new File(fileName)).exists();

    assertTrue(String.format("Table must be written to file '%s'.", fileName), fileExists);
  }

  /**
   * Private helper method to test deserialization of Table object.
   */
  private void testTableLoad()
  {
    Table table = Table.loadTable("Studenten");

    assertNotNull("Table object must not be null.", table);
    assertTrue("Output of table must not be empty.", !("").equals(table.toString()));
    assertTrue("Output must contain data of table.", table.toString().contains("Serialize"));

    this.printTable(table);
  }

  /**
   * Private helper method to create a Table object for testing.
   */
  private Table createTestTable()
  {
    Table table = new Table("Studenten", "s", new String[] { "Name", "Matrikelnummer", "Studiengang", "Semester" });

    table.addRow(new String[] { "Alice", "12345", "Medizin", "16" });
    table.addRow(new String[] { "Bob", "67890", "Jura", "3" });
    table.addRow(new String[] { "Homer", "111111", "Kernphysik", "27" });
    table.addRow(new String[] { "Marge", "222222", "Kunst", "7" });
    table.addRow(new String[] { "Bart", "333333", "Informatik", "4" });
    table.addRow(new String[] { "Lisa", "444444", "Kunst", "3" });
    table.addRow(new String[] { "Maggie", "555555", "Informatik", "1" });

    return table;
  }

  /**
   * Private helper method to create a relational algebra tree.
   */
  private ITreeNode createRelAlgTree() throws Exception
  {
    EqualityExpression equalityExpression = new EqualityExpression();
    equalityExpression.setFirstExpression(new PrimaryExpression("Hobbies.Lastname", false));
    equalityExpression.setSecondExpression(new PrimaryExpression("Persons.Lastname", false));
    equalityExpression.setOperator("=");
    
    AndExpression andExpression = new AndExpression(equalityExpression);
    
    equalityExpression = new EqualityExpression();
    equalityExpression.setFirstExpression(new PrimaryExpression("Vanessa", true));
    equalityExpression.setSecondExpression(new PrimaryExpression("Firstname", false));
    equalityExpression.setOperator("=");

    OrExpression orExpression = new OrExpression(equalityExpression);
    andExpression.getExpressions().add(orExpression);
    Selection selection = new Selection(andExpression);
    
    Projection projection = new Projection();
    projection.setColumnNames(new String[]{"Hobbies.Lastname", "Persons.Lastname"});
    projection.setChild(selection);

    CrossProduct cross = new CrossProduct();    
    selection.setChild(cross);
    
    CreateTable createTableOperation = new CreateTable();
    createTableOperation.setName("Persons");
    createTableOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });
    Table table1 = createTableOperation.execute();
    table1.write();

    Insert insertOperation = new Insert();
    insertOperation.setName("Persons");
    insertOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });
    insertOperation.setValues(new String[] { "Max", "Mustermann", "42" });
    table1 = insertOperation.execute();
    table1.write();

    insertOperation = new Insert();
    insertOperation.setName("Persons");
    insertOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });
    insertOperation.setValues(new String[] { "Vanessa", "Meier", "21" });
    table1 = insertOperation.execute();
    table1.write();
    
    cross.setChild(table1.toRelation());

    createTableOperation = new CreateTable();
    createTableOperation.setName("Hobbies");
    createTableOperation.setColumnNames(new String[] { "Lastname", "Hobby" });
    Table table2 = createTableOperation.execute();
    table2.write();

    insertOperation = new Insert();
    insertOperation.setName("Hobbies");
    insertOperation.setColumnNames(new String[] { "Lastname", "Hobby" });
    insertOperation.setValues(new String[] { "Meier", "Fishing" });
    table2 = insertOperation.execute();
    table2.write();

    insertOperation = new Insert();
    insertOperation.setName("Hobbies");
    insertOperation.setColumnNames(new String[] { "Lastname", "Hobby" });
    insertOperation.setValues(new String[] { "Mustermann", "Golf" });
    table2 = insertOperation.execute();
    table2.write();
    
    cross.setSecondChild(table2.toRelation());

    return projection;
  }

  /**
   * Private helper method to execute plan and persist results.
   */
  private Table executePlan(final TableOperation operation) throws Exception
  {
    Table result = operation.execute();
    result.write();
    
    return result;
  }

  /**
   * Private helper method to print a unit test caption.
   */
  private void printCaption(final String caption)
  {
    System.out.println("\n************************************************");
    System.out.println("* " + caption);
    System.out.println("************************************************\n");
  }

  /**
   * Private helper method to print Table object.
   */
  private void printTable(final Table table)
  {
    // Print table object
    System.out.println("\n\n" + table + "\n\n");
  }
}
