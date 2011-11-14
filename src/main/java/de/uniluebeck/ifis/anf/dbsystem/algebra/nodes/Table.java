package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author seidel
 */
public class Table implements Serializable
{
  /** Absolute path to folder, where tables are stored in file system (with trailing slash!) */
  public static final String DATABASE_PATH = System.getProperty("user.home") + "/anfrage/repo/database/";

  /** File extension for database tables in file system (with leading dot!) */
  public static final String DATABASE_TABLE_FILE_EXTENSION = ".dbt";

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

  public boolean isDropped()
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
  
  /**
   * Load Table object from file.
   * 
   * @param tableName Name of desired table
   * 
   * @return In-memory Table object
   */
  public static Table loadTable(final String tableName)
  {
    FileInputStream fileInputStream = null;
    ObjectInputStream objectInputStream = null;
    Table result = null;

    try
    {
      // Load input stream from file
      fileInputStream = new FileInputStream(Table.DATABASE_PATH +
              System.getProperty("file.separator") + tableName + Table.DATABASE_TABLE_FILE_EXTENSION);
      objectInputStream = new ObjectInputStream(fileInputStream);

      // Deserialize object from stream
      result = (Table)objectInputStream.readObject();
      objectInputStream.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Write current Table object to file.
   */
  public void write() throws Exception
  {
    String pathToFile = Table.DATABASE_PATH + System.getProperty("file.separator") + this.getName() + Table.DATABASE_TABLE_FILE_EXTENSION;
    
    if (!(new File(Table.DATABASE_PATH)).exists())
    {
      throw new FileNotFoundException(String.format("Database folder '%s' does not exist. " +
              "Please create it and try again.", Table.DATABASE_PATH));
    }
    
    // Delete table from database, if drop-flag is set
    if (this.drop)
    {
      if (!(new File(pathToFile)).delete())
      {
        throw new IllegalStateException(String.format("Could not drop table '%s'", this.getName()));
      }
    }
    // Save table otherwise
    else
    {
      FileOutputStream fileOutputStream = null;
      ObjectOutputStream objectOutputStream = null;

      try
      {
        // Open stream for object write-out
        fileOutputStream = new FileOutputStream(Table.DATABASE_PATH +
                System.getProperty("file.separator") + this.getName() + Table.DATABASE_TABLE_FILE_EXTENSION);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);

        // Serialize object to stream
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
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

        // Add space in every row
        if (i < this.rows.size())
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