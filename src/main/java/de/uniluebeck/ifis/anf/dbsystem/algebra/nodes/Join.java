package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

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
	public Relation evaluate() throws Exception {
		Relation crossRelation = super.evaluate();
		Relation result = new Relation();
		result.setColumnNames(crossRelation.getColumnNames());
		result.setName(crossRelation.getName());
		result.setAlias(crossRelation.getAlias());
		result.setDrop(false);
		for (Row row : crossRelation.getRows()) {
			if (expression.evaluate(row)) {
				result.getRows().add(row);
			}
		}
		return result;
	}

  /**
   * Calculate costs for join:
   *
   * Rows_s(T1) * Rows_s(T2) * (Columns(T1) + Columns(T2))
   *
   * @return Costs of join
   *
   * @throws Exception Error during evaluation
   */
  @Override
  public int getCosts() throws Exception
  {
	int oldCost = getChild().getCosts() + getSecondChild().getCosts();
	  
    Relation rel1 = getChild().evaluate();
    Relation rel2 = getSecondChild().evaluate();

    return oldCost + this.evaluate().getRows().size() +
            (rel1.getColumnNames().length + rel2.getColumnNames().length);
  }
}
