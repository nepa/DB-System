package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class Selection extends OneChildNode
{
  protected AndExpression expression;
  
  public AndExpression getExpression()
  {
    return expression;
  }
  
  public void setExpression(AndExpression expression)
  {
    this.expression = expression;
  }
  
  public Relation evaluate()
  {
    Relation relation = this.getChild().evaluate();
    
    Relation result = new Relation();
    
    for (Row row: relation.getRows())
    {
      if (this.expression.evaluate(row))
      {
        result.getRows().add(row);
      }
    }
    
    return result;
  }
}
