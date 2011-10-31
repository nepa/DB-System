package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public class PrimaryExpression implements IExpression
{
  protected boolean isConstant;
  
  protected String value;

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
    //String alias = value.split(".")[0];
    String tupleName = value;
    
      
      for (int i = 0; i < relation.tupleNames.length; ++i)
      {
        if (relation.tupleNames[i].equals(tupleName))
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
    
    return (firstResults != null) ? firstResults : secondResults;
  }
}
