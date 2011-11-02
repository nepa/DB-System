package de.uniluebeck.ifis.anf.dbsystem.algebra;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author seidel
 */
public class Table implements Serializable
{
  protected static String databasePath;

  protected String name;

  protected String alias;

  protected boolean drop;

  protected String[] columnNames;

  protected ArrayList<String[]> rows;

  public Table(final String name, String alias, final String[] columnNames)
  {
    this.setName(name);
    this.setAlias(alias);
    this.drop = false;
    this.setColumnNames(columnNames);
    this.rows = new ArrayList<String[]>();
  }

  public String getAlias()
  {
    return alias;
  }

  public void setAlias(String alias)
  {
    this.alias = alias;
  }

  public String[] getColumnNames()
  {
    return columnNames;
  }

  public void setColumnNames(String[] columnNames)
  {
    this.columnNames = columnNames;
  }

  public static String getDatabasePath()
  {
    return databasePath;
  }

  public static void setDatabasePath(String databasePath)
  {
    Table.databasePath = databasePath;
  }

  public boolean isDrop()
  {
    return drop;
  }

  public void setDrop(boolean drop)
  {
    this.drop = drop;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ArrayList<String[]> getRows()
  {
    return rows;
  }

  public void setRows(ArrayList<String[]> rows)
  {
    this.rows = rows;
  }

  public static Table loadTable(String tableName)
  {
    // TODO
    return null;
  }

  public void write()
  {
    // TODO
  }

  public String[] getRow(int index)
  {
    return this.rows.get(index);
  }

  public void addRow(String[] row)
  {
    this.rows.add(row);
  }

  public void deleteRow(int index)
  {
    this.rows.remove(index);
  }

  public Table projectTo(String[] columnNames)
  {
    // TODO
    return null;
  }

  public Table select(AndExpression selectExpression)
  {
    // TODO
    return null;
  }

  public Table join(Table joinPartner, AndExpression joinExpression)
  {
    // TODO
    return null;
  }

  public Table cross(Table crossPartner)
  {
    // TODO
    return null;
  }

  public Relation toRelation()
  {
    Relation relation = new Relation();

    relation.setName(this.name);
    relation.setAlias(this.alias);
    relation.setColumnNames(this.columnNames);

    for (String[] tableRow: rows)
    {
      Row row = new Row();
      row.name = this.name;
      row.alias = this.alias;
      row.tupleNames = this.columnNames;
      row.tuple = tableRow;

      relation.getRows().add(row);
    }

    return relation;
  }

  /**
   * Pretty-print meta information and content of table.
   * 
   * @return String with table data
   */
  @Override
  public String toString()
  {
    // Amount of characters that are shown from content
    int n = 20;

    // Generate header for table output
    String tableHeader = "| ";
    String columnName = "";
    for (int i = 0; i < this.columnNames.length; ++i)
    {
      columnName = (this.columnNames[i].length() > n ? this.columnNames[i].substring(0, n - 3) + "..." : this.columnNames[i]);
      tableHeader += String.format("%-" + n + "s |", columnName);

      if (i < this.columnNames.length - 1)
      {
        tableHeader += " ";
      }
    }

    // Create line of '-' characters
    String line = "";
    for (int i = 0; i < tableHeader.length(); ++i)
    {
      line += "-";
    }

    // Generate output with content of table
    String tableContent = "";
    String rowContent = "";
    for (int i = 0; i < this.rows.size(); ++i)
    {
      tableContent += "| ";
      for (int j = 0; j < this.rows.get(i).length; ++j)
      {
        rowContent = (this.rows.get(i)[j].length() > n ? this.rows.get(i)[j].substring(0, n - 3) + "..." : this.rows.get(i)[j]);
        tableContent += String.format("%-" + n + "s |", rowContent);

        if (j < this.rows.get(i)[j].length() - 1)
        {
          tableContent += " ";
        }
      }
      tableContent += "\n";
    }

    String result = String.format("Table: '%s' alias '%s' [drop = %b]\n\n%s\n%s\n%s\n%s%s",
            this.name, this.alias, this.drop, line, tableHeader, line, tableContent, line);

    return result;
  }
}
