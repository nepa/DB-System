package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public class Join extends CrossProduct
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
  
  @Override
  public Relation evaluate(){
    Relation rel = super.evaluate();
    Relation result = new Relation();
    for (Row row : rel.getRows()){
      if (expression.evaluate(row)){
        result.getRows().add(row);
      }
    }
    return result;
  }
}
