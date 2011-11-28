package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class Selection extends OneChildNode
{
  protected AndExpression expression;

  public Selection(){
	  
  }
  
  public Selection(AndExpression expression){
	  this.expression = expression;
  }
  
  public AndExpression getExpression()
  {
    return expression;
  }

  public void setExpression(AndExpression expression)
  {
    this.expression = expression;
  }

  @Override
  public Relation evaluate() throws Exception
  {
    Relation relation = this.getChild().evaluate();

    Relation result = new Relation();
    result.setName(relation.getName());
    result.setAlias(relation.getAlias());
    result.setDrop(relation.isDropped());
    result.setColumnNames(relation.getColumnNames());

    for (Row row: relation.getRows())
    {
      if (this.expression.evaluate(row))
      {
        result.getRows().add(row);
      }
    }

    return result;
  }

  /**
   * Calculate costs for selection:
   *
   * Rows_s(T) * Columns(T)
   *
   * @return Costs for selection
   *
   * @throws Exception Error during evaluation
   */
  @Override
  public int getCosts() throws Exception
  {
    int oldCost = getChild().getCosts();
    
    return oldCost + this.getChild().evaluate().getRows().size() * this.getChild().evaluate().getColumnNames().length;
  }
}
