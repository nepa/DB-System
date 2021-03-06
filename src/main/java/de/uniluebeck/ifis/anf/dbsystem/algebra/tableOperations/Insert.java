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
  public Table execute() throws Exception
  {
    Table table = Table.loadTable(this.name);

    String[] newRow = new String[table.getColumnNames().length];
    for (int i = 0; i < newRow.length; i++)
    {
      newRow[i] = "";
    }
    if (this.getColumnNames() == null)
    {
      this.setColumnNames(table.getColumnNames());
    }
    for (int i = 0; i < table.getColumnNames().length; ++i)
    {
      for (int j = 0; j < this.columnNames.length; ++j)
      {
        String columnName = table.getColumnNames()[i];
        if (columnName.contains(".")){
          columnName = columnName.split("\\.")[1];
        }
        
        if (columnName.equals(this.columnNames[j]))
        {
          newRow[i] = this.values[j];
        }
      }
    }

    table.addRow(newRow);
    
//     table.write(); // TODO: Remove

    return table;
  }

  @Override
  public Relation evaluate() throws Exception
  {
    return this.execute().toRelation();
  }
}
