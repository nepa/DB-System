package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class Row
{
  protected String name;

  protected String alias;

  protected String[] tuple;

  protected String[] columnNames;

  public String getAlias()
  {
    return alias;
  }

  public void setAlias(String alias)
  {
    this.alias = alias;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String[] getTuple()
  {
    return tuple;
  }

  public void setTuple(String[] tuple)
  {
    this.tuple = tuple;
  }

  public String[] getTupleNames()
  {
    return columnNames;
  }

  public void setTupleNames(String[] tupleNames)
  {
    this.columnNames = tupleNames;
  }

  /**
   * check if the column with the given index has the given name, take into account
   * that a . can be in name but doesn't need to be
   * @param index
   * @param name
   * @return
   */
  public boolean columnNameEquals(int index, String name)
  {
    if (name.contains("."))
    {
      return columnNames[index].equals(name);
    }
    else
    {
      return this.columnNames[index].split("\\.")[1].equals(name);
    }
  }
}
