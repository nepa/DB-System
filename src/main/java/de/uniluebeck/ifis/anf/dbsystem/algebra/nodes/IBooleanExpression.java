package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.List;

/**
 * @author seidel
 */
public interface IBooleanExpression extends IExpression
{
  public Boolean evaluate(Row relation);

  public Boolean evaluate(Row firstRelation, Row secondRelation);
  
  public List<String> getAttributes();
}
