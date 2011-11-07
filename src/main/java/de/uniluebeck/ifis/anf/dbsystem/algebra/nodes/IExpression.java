package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public interface IExpression
{
  public Object evaluate(Row relation);

  public Object evaluate(Row firstRelation, Row secondRelation);
}
