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
    result.setColumnNames(this.columnNames);
    
    for (Row row: relation.getRows())
    {
      Row newRow = new Row();
      newRow.name = row.name;
      newRow.alias = row.alias;
      newRow.tupleNames = this.columnNames;
      newRow.tuple = new String[this.columnNames.length];
      for (int i = 0; i < this.columnNames.length; ++i){
        for (int j = 0; j < row.tupleNames.length; ++j){
          if (newRow.tupleNames[i].equals(row.tupleNames[j])){
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
    return this.evaluate().getRows().size() * this.columnNames.length;
  }
}
