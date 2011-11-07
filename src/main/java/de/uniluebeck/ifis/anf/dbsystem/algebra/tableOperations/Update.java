package de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Row;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;
import java.util.List;

/**
 * @author seidel
 */
public class Update extends Delete
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
    
    Relation relation = table.toRelation();
    List<Row> rows = this.getRelevantRows(relation);
    
    // Update relevant rows
    for (Row row: rows)
    {
      for (int i = 0; i < row.getTuple().length; ++i)
      {
        for (int j = 0; j < this.columnNames.length; ++j)
        {
          if (row.getTupleNames()[i].equals(this.columnNames[j]))
          {
            row.getTuple()[i] = this.values[j];
          }
        }
      }
    }
    
    table = relation.toTable();
    
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
}
