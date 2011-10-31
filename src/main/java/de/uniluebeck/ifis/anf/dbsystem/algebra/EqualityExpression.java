package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public class EqualityExpression implements IBooleanExpression
{
  protected PrimaryExpression firstExpression;
  
  protected PrimaryExpression secondExpression;
  
  protected String operator;

  public PrimaryExpression getFirstExpression()
  {
    return firstExpression;
  }

  public void setFirstExpression(PrimaryExpression firstExpression)
  {
    this.firstExpression = firstExpression;
  }

  public String getOperator()
  {
    return operator;
  }

  public void setOperator(String operator)
  {
    if (operator.equals("=") || operator.equals("!=") ||
        operator.equals(">") || operator.equals("<") ||
        operator.equals("<=") || operator.equals(">="))
    {
      this.operator = operator;
    }
    else
    {
      throw new IllegalArgumentException(String.format("Illegal operator '%s' in EqualityExpression class.", operator));
    }
  }

  public PrimaryExpression getSecondExpression()
  {
    return secondExpression;
  }

  public void setSecondExpression(PrimaryExpression secondExpression)
  {
    this.secondExpression = secondExpression;
  }

  public Boolean evaluate(Row relation)
  {
    String value1 = (String)firstExpression.evaluate(relation);
    String value2 = (String)secondExpression.evaluate(relation);
    return evaluateOperator(value1, value2);
  }

  public Boolean evaluate(Row firstRelation, Row secondRelation)
  {
    String value1 = (String)firstExpression.evaluate(firstRelation, secondRelation);
    String value2 = (String)secondExpression.evaluate(firstRelation, secondRelation);
    return evaluateOperator(value1, value2);
  }
  
  private boolean evaluateOperator(String value1, String value2){
    int result = value1.compareTo(value2);
    if (this.operator.equals("=")){
      return result == 0;
    } else if (this.operator.equals("!=")){
      return result != 0;
    } else if (this.operator.equals(">")){
      return result > 0;
    } else if (this.operator.equals("<")){
      return result < 0;
    } else if (this.operator.equals("<=")){
      return result <= 0;
    } else if (this.operator.equals(">=")){
      return result >= 0;
    } else {
      return false;
    }
  }
}
