package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.List;
import java.util.ArrayList;

/**
 * @author seidel
 */
public class OrExpression implements IBooleanExpression {
	protected List<EqualityExpression> expressions;

	public OrExpression() {
		expressions = new ArrayList<EqualityExpression>();
	}

	public OrExpression(final List<EqualityExpression> expressions) {
		this.setExpressions(expressions);
	}

	public OrExpression(final EqualityExpression expression) {
		List<EqualityExpression> list = new ArrayList<EqualityExpression>();
		list.add(expression);

		this.setExpressions(list);
	}

	public List<EqualityExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<EqualityExpression> expressions) {
		this.expressions = expressions;
	}

	public Boolean evaluate(Row relation) {
		for (EqualityExpression expr : expressions) {
			if (expr.evaluate(relation)) {
				return true;
			}
		}
		return false;
	}

	public Boolean evaluate(Row firstRelation, Row secondRelation) {
		for (EqualityExpression expr : expressions) {
			if (expr.evaluate(firstRelation, secondRelation)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> getAttributes() {
		List<String> result = new ArrayList<String>();
		for (EqualityExpression expr : expressions){
			result.addAll(expr.getAttributes());
		}
		return result;
	}
}
