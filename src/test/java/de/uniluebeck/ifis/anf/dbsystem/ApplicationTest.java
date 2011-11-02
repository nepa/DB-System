package de.uniluebeck.ifis.anf.dbsystem;

import org.junit.Test;
import static org.junit.Assert.*;

import de.uniluebeck.ifis.anf.dbsystem.algebra.Table;

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
    Table table = new Table("Studenten", "s", new String[] { "Name", "Matrikelnummer", "Studiengang", "Semester" });
    table.addRow(new String[] { "Alice", "12345", "Medizin", "16" });
    table.addRow(new String[] { "Bob", "67890", "Jura", "3" });
    table.addRow(new String[] { "Homer", "111111", "Kernphysik", "27" });
    table.addRow(new String[] { "Marge", "222222", "Kunst", "7" });
    table.addRow(new String[] { "Bart", "333333", "Informatik", "4" });
    table.addRow(new String[] { "Lisa", "444444", "Kunst", "3" });
    table.addRow(new String[] { "Maggie", "555555", "Informatik", "1" });

    // Print table object
    System.out.println("\n\n" + table + "\n\n");
    
    // Test if output is not empty and contains data
    assertTrue("Output of table must not be empty.", !("").equals(table.toString()));
    assertTrue("Output must contain data of table.", table.toString().contains("Bob"));
  }
}
