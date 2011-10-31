package de.uniluebeck.ifis.anf.dbsystem.algebra;

import java.util.List;

/**
 * @author seidel
 */
public class OrExpression implements IBooleanExpression
{
  protected List<EqualityExpression> expressions;

  public List<EqualityExpression> getExpressions()
  {
    return expressions;
  }

  public void setExpressions(List<EqualityExpression> expressions)
  {
    this.expressions = expressions;
  }

  public Boolean evaluate(Row relation)
  {
    for (EqualityExpression expr : expressions){
      if (expr.evaluate(relation)){
        return true;
      }
    }
    return false;
  }

  public Boolean evaluate(Row firstRelation, Row secondRelation)
  {
    for (EqualityExpression expr : expressions){
      if (expr.evaluate(firstRelation, secondRelation)){
        return true;
      }
    }
    return false;
  }
}
