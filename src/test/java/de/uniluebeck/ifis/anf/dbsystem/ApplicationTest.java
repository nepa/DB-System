package de.uniluebeck.ifis.anf.dbsystem;

import org.junit.Test;
import static org.junit.Assert.*;

import de.uniluebeck.ifis.anf.dbsystem.algebra.Table;
import java.io.File;

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

    // Print table object
    System.out.println("\n\n" + table + "\n\n");
    
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
   * Private helper method to test serialization of Table objects.
   */
  private void testTableWrite() throws Exception
  {
    Table table = this.createTestTable();

    System.out.println("Adding new row...");
    table.addRow(new String[] { "Serialize", "0", "Test", "0" });

    table.write();

    String fileName = Table.DATABASE_PATH + System.getProperty("file.separator") +
            table.getName() + Table.DATABASE_TABLE_FILE_EXTENSION;
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

    // Print table object
    System.out.println("\n\n" + table + "\n\n");
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
}
