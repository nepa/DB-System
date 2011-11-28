package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.List;
import java.util.ArrayList;

/**
 * @author seidel
 */
public class AndExpression implements IBooleanExpression {
	protected List<OrExpression> expressions;

	public AndExpression() {
		this.expressions = new ArrayList<OrExpression>();
	}

	public AndExpression(final List<OrExpression> expressions) {
		this.setExpressions(expressions);
	}

	public AndExpression(final OrExpression expression) {
		List<OrExpression> list = new ArrayList<OrExpression>();
		list.add(expression);

		this.setExpressions(list);
	}

	public AndExpression(final EqualityExpression expression) {
		List<OrExpression> list = new ArrayList<OrExpression>();
		list.add(new OrExpression(expression));

		this.setExpressions(list);
	}

	public List<OrExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<OrExpression> expressions) {
		this.expressions = expressions;
	}

	public Boolean evaluate(Row relation) {
		for (OrExpression expr : expressions) {
			if (!expr.evaluate(relation)) {
				return false;
			}
		}
		return true;
	}

	public Boolean evaluate(Row firstRelation, Row secondRelation) {
		for (OrExpression expr : expressions) {
			if (!expr.evaluate(firstRelation, secondRelation)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getAttributes() {
		List<String> result = new ArrayList<String>();
		for (OrExpression expr : expressions) {
			result.addAll(expr.getAttributes());
		}
		return result;
	}
}
