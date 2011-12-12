package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class PrimaryExpression implements IExpression
{
  protected boolean isConstant;
  
  protected String value;
  
  public PrimaryExpression()
  {
    this.setValue("");
    this.setIsConstant(false);
  }
  
  public PrimaryExpression(final String value, final boolean isConstant)
  {
    this.setValue(value);
    this.setIsConstant(isConstant);
  }

  public boolean isIsConstant()
  {
    return isConstant;
  }

  public void setIsConstant(boolean isConstant)
  {
    this.isConstant = isConstant;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public Object evaluate(Row relation)
  {
    if (isIsConstant()){
      return value;
    }    
      
    for (int i = 0; i < relation.columnNames.length; ++i)
    {
      if (relation.columnNameEquals(i, value))
      {
        return relation.getTuple()[i];
      }
    }
    return null;
  }

  public Object evaluate(Row firstRelation, Row secondRelation)
  {
    String firstResults = (String)this.evaluate(firstRelation);
    String secondResults = (String)this.evaluate(secondRelation);
    
    return ((firstResults != null) ? firstResults : secondResults);
  }
}
