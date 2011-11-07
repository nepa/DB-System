package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class Row
{
  protected String name;
  
  protected String alias;
  
  protected String[] tuple;
  
  protected String[] tupleNames;

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
    return tupleNames;
  }

  public void setTupleNames(String[] tupleNames)
  {
    this.tupleNames = tupleNames;
  }
}
