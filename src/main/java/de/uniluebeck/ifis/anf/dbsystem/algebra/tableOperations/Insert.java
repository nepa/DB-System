package de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;

/**
 * @author seidel
 */
public class Insert extends TableOperation
{
  protected String[] columnNames;

  protected String[] values;

  public String[] getColumnNames()
  {
    return columnNames;
  }

  public void setColumnNames(String[] columnNames)
  {
    this.columnNames = columnNames;
  }

  public String[] getValues()
  {
    return values;
  }

  public void setValues(String[] values)
  {
    this.values = values;
  }

  @Override
  public Table execute()
  {
    Table table = Table.loadTable(this.name);

    String[] newRow = new String[table.getColumnNames().length];
    for (int i = 0; i < newRow.length; i++)
    {
      newRow[i] = "";
    }
    if (this.getColumnNames() == null){
    	this.setColumnNames(table.getColumnNames());
    }
    for (int i = 0; i < table.getColumnNames().length; ++i)
    {
      for (int j = 0; j < this.columnNames.length; ++j)
      {
        if (table.getColumnNames()[i].equals(this.columnNames[j]))
        {
          newRow[i] = this.values[j];
        }
      }
    }

    table.addRow(newRow);
    
    try
    {
      table.write();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return table;
  }
  
  @Override
  public Relation evaluate()
  {
    return this.execute().toRelation();
  }
}
