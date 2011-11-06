package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public class CreateTable extends TableOperation
{
  protected String[] columnNames;

  public String[] getColumnNames()
  {
    return columnNames;
  }

  public void setColumnNames(String[] columnNames)
  {
    this.columnNames = columnNames;
  }

  @Override
  public Table execute()
  {
    Table table = new Table(this.name, this.name, this.columnNames);

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
