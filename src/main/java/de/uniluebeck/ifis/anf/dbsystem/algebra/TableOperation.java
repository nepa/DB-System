package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
abstract public class TableOperation
{
  protected String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
  
  abstract public Table execute();  
}
