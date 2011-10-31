package de.uniluebeck.ifis.anf.dbsystem.algebra;

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
    // TODO
    throw new UnsupportedOperationException("Not supported yet.");
  }  
}
