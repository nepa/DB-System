package de.uniluebeck.ifis.anf.dbsystem;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.*;
import de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.*;

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
    System.out.println("Serializing table...");
    this.testTableWrite();

    System.out.print("Deserializing table...");
    this.testTableLoad();
  }

  /**
   * Test table operations.
   */
  @Test(timeout = 1000)
  public void testTableOperations()
  {
    String tableName = "Person";
    
    // Test CREATE TABLE operation
    CreateTable createTableOperation = new CreateTable();
    createTableOperation.setName(tableName);
    createTableOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });

    System.out.println(String.format("Creating table '%s'...", tableName));
    Table table = createTableOperation.execute();
    assertNotNull("Table object must not be null.", table);
    this.printTable(table);
    
    // Test INSERT operation
    Insert insertOperation = new Insert();
    insertOperation.setName(tableName);
    insertOperation.setColumnNames(new String[] { "Firstname", "Lastname", "Age" });
    insertOperation.setValues(new String[] { "Max", "Mustermann", "42" });
    
    System.out.println("Inserting entry 'Max'...");
    table = insertOperation.execute();
    assertTrue("Table must contain entry that was inserted.", table.toString().contains("Max"));
    this.printTable(table);
    
    // Test another INSERT operation
    insertOperation = new Insert();
    insertOperation.setName(tableName);
    insertOperation.setColumnNames(new String[] { "Firstname", "Age" });
    insertOperation.setValues(new String[] { "Vanessa", "27" });

    System.out.println("Inserting entry 'Vanessa'...");
    table = insertOperation.execute();
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
    table = updateOperation.execute();
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
    table = deleteOperation.execute();
    assertTrue("Table must not contain deleted entry anymore.", !table.toString().contains("Mustermann"));
    this.printTable(table);
    
    // Test DROP TABLE operation
    DropTable dropTableOperation = new DropTable();
    dropTableOperation.setName(tableName);
    
    System.out.println(String.format("Dropping table '%s'...", tableName));
    table = dropTableOperation.execute();
    assertTrue("Drop flag must be set for table.", table.isDropped());
    this.printTable(table);
  }
  
  /**
   * Test execution of SQL queries.
   */
  @Test(timeout = 10000)
  public void testSQLQueries()
  {
    Application.createKundenDB();

    Table table = Application.executeQuery("select Titel from Buch where ID = \"Buch2\"");
    this.printTable(table);
    assertTrue(table.getRows().size() == 1);
    assertTrue(table.getRow(0)[0].equals("\"Kennedys Hirn\""));

    Application.executeQuery("update Kunde set Vorname = \"Max\", Nachname = \"Mustermann\" where ID = \"Kunde1\";");
    table = Application.executeQuery("select Nachname from Kunde where ID = \"Kunde1\";");
    this.printTable(table);
    assertTrue(table.getRows().size() == 1);
    assertTrue(table.getRow(0)[0].equals("\"Mustermann\""));
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
   * Private helper method to print Table object.
   */
  private void printTable(final Table table)
  {
    // Print table object
    System.out.println("\n\n" + table + "\n\n");
  }
}
