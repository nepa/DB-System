package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class Projection extends OneChildNode
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
  public Relation evaluate() throws Exception
  {
    Relation relation = this.getChild().evaluate();
    Relation result = new Relation();
    result.setAlias(relation.getAlias());
    result.setName(relation.getName());
    result.setDrop(false);
    String[] columnNames = new String[this.columnNames.length];
    for (int i = 0; i < columnNames.length; ++i)
    {
      if (this.columnNames[i].contains("."))
      {
        columnNames[i] = this.columnNames[i];
      }
      else
      {
        columnNames[i] = result.getAlias() + "." + this.columnNames[i];
      }
    }
    result.setColumnNames(columnNames);

    for (Row row: relation.getRows())
    {
      Row newRow = new Row();
      newRow.name = row.name;
      newRow.alias = row.alias;
      newRow.columnNames = result.getColumnNames();
      newRow.tuple = new String[this.columnNames.length];
      for (int i = 0; i < this.columnNames.length; ++i)
      {
        for (int j = 0; j < row.columnNames.length; ++j)
        {
          if (newRow.columnNames[i].equals(row.columnNames[j]))
          {
            newRow.tuple[i] = row.tuple[j];
          }
        }
      }
      result.getRows().add(newRow);
    }

    return result;
  }

  /**
   * Calculate costs for projection:
   * 
   * Rows(T) * n
   *
   * Where n is the number of projected columns.
   *
   * @return Costs of projection
   *
   * @throws Exception Error during evaluation
   */
  @Override
  public int getCosts() throws Exception
  {
    int oldCost = getChild().getCosts();
    return oldCost + this.getChild().evaluate().getRows().size() * this.columnNames.length;
  }
}
