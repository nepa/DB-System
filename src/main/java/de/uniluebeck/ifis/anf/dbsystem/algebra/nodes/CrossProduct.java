package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class CrossProduct extends TwoChildNode
{
  @Override
  public Relation evaluate() throws Exception
  {
    Relation rel1 = getChild().evaluate();
    Relation rel2 = getSecondChild().evaluate();
    Relation result = new Relation();

    result.setName(rel1.getName() + "x" + rel2.getName());
    result.setAlias(rel1.getAlias() + "x" + rel2.getAlias());
    result.setDrop(false);
    result.setColumnNames(new String[rel1.getColumnNames().length + rel2.getColumnNames().length]);
    for (int i = 0; i < rel1.getColumnNames().length; ++i)
    {
      result.getColumnNames()[i] = rel1.getColumnNames()[i];
    }
    for (int i = 0; i < rel2.getColumnNames().length; ++i)
    {
      result.getColumnNames()[i + rel1.getColumnNames().length] = rel2.getColumnNames()[i];
    }

    for (Row row1: rel1.getRows())
    {
      for (Row row2: rel2.getRows())
      {
        Row newRow = new Row();
        newRow.name = row1.name + "x" + row2.name;
        newRow.alias = row1.alias + "x" + row2.alias;
        newRow.tuple = new String[row1.tuple.length + row2.tuple.length];
        newRow.tupleNames = new String[row1.tupleNames.length + row2.tupleNames.length];
        for (int i = 0; i < row1.tupleNames.length; ++i)
        {
          newRow.tuple[i] = row1.tuple[i];
          newRow.tupleNames[i] = row1.tupleNames[i];
        }
        for (int i = 0; i < row2.tupleNames.length; ++i)
        {
          newRow.tuple[i + row1.tupleNames.length] = row2.tuple[i];
          newRow.tupleNames[i + row1.tupleNames.length] = row2.tupleNames[i];
        }
        result.getRows().add(newRow);
      }
    }

    return result;
  }
  
  /**
   * Calculate costs for cross product:
   *
   * Rows(T1) * Rows(T2) * (Columns(T1) + Columns(T2))
   * 
   * @return Costs for cross product
   * 
   * @throws Exception Error during evaluation
   */
  @Override
  public int getCosts() throws Exception
  {
    Relation rel1 = getChild().evaluate();
    Relation rel2 = getSecondChild().evaluate();

    return rel1.getRows().size() * rel2.getRows().size()
            * (rel1.getColumnNames().length + rel2.getColumnNames().length);
  }
}
