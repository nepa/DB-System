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
  public Table execute() throws Exception
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
          // Remove alias from column name, if any is set
          String columnName = (row.getTupleNames()[i].contains(".") ? row.getTupleNames()[i].split("\\.")[1] : row.getTupleNames()[i]);
          
          if (columnName.equals(this.columnNames[j]))
          {
            row.getTuple()[i] = this.values[j];
          }
        }
      }
    }

    table = relation.toTable();
//     table.write(); // TODO: Remove

    return table;
  }
}
