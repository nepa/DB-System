package de.uniluebeck.ifis.anf.dbsystem.algebra;

import java.util.List;

/**
 * @author seidel
 */
public class AndExpression implements IBooleanExpression
{
  protected List<OrExpression> expressions;

  public List<OrExpression> getExpressions()
  {
    return expressions;
  }

  public void setExpressions(List<OrExpression> expressions)
  {
    this.expressions = expressions;
  }

  public Boolean evaluate(Row relation)
  {
    for (OrExpression expr : expressions){
      if (!expr.evaluate(relation)){
        return false;
      }
    }
    return true;
  }

  public Boolean evaluate(Row firstRelation, Row secondRelation)
  {
    for (OrExpression expr : expressions){
      if (!expr.evaluate(firstRelation, secondRelation)){
        return false;
      }
    }
    return true;
  }
}
