package de.uniluebeck.ifis.anf.dbsystem.algebra;

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

    String[] newRow = new String[table.columnNames.length];
    for (int i = 0; i < newRow.length; i++){
      newRow[i] = "";
    }
    for (int i = 0; i < table.columnNames.length; ++i)
    {
      for (int j = 0; j < this.columnNames.length; ++j)
      {
        if (table.columnNames[i].equals(this.columnNames[j]))
        {
          newRow[i] = this.values[j];
        }
      }
    }

    table.addRow(newRow);
    return table;
  }
}
