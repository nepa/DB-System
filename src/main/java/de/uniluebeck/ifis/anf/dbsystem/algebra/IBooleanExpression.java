package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public interface IBooleanExpression extends IExpression
{
  public Boolean evaluate(Row relation);

  public Boolean evaluate(Row firstRelation, Row secondRelation);
}
