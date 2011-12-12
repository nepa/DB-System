package de.uniluebeck.ifis.anf.dbsystem.optimierung;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.CrossProduct;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.EqualityExpression;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Join;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OneChildNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Selection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.TwoChildNode;

public class DetectJoins implements IOptimization {

	@Override
	public ITreeNode optimize(ITreeNode executionPlan) throws Exception {
		if (executionPlan.getClass() == Selection.class && ((Selection)executionPlan).getChild().getClass() == CrossProduct.class){
			Selection selection = (Selection) executionPlan;
			CrossProduct cross = (CrossProduct) selection.getChild();
			if (canMerge(selection, cross)) {
				OneChildNode parent = selection.getParent();
				boolean secondChild = (parent == null) ? false : (parent
						.getChild() != selection);
				Join join = new Join();
				join.setExpression(selection.getExpression());
				join.setChild(cross.getChild());
				join.setSecondChild(cross.getSecondChild());
				if (parent == null) {
					join.setParent(null);
				} else {
					if (secondChild) {
						TwoChildNode twoChild = (TwoChildNode) parent;
						twoChild.setSecondChild(join);
					} else {
						parent.setChild(join);
					}
				}

				executionPlan = join;
			}

		}
		if (executionPlan instanceof OneChildNode){
			OneChildNode oneChild = (OneChildNode) executionPlan;
			oneChild.setChild(optimize(oneChild.getChild()));
		}
		if (executionPlan instanceof TwoChildNode){
			TwoChildNode twoChild = (TwoChildNode) executionPlan;
			twoChild.setSecondChild(optimize(twoChild.getSecondChild()));
		}
		return executionPlan;
	}
	
	private boolean canMerge(Selection selection, CrossProduct cross) throws Exception{
		
		//only one primary expression
		if (selection.getExpression().getExpressions().size() != 1){
			return false;
		} 
		if (selection.getExpression().getExpressions().get(0).getExpressions().size() != 1){
			return false;
		}
		
		EqualityExpression expression = selection.getExpression().getExpressions().get(0).getExpressions().get(0);
		//operator needs to be '='
		if (!expression.getOperator().equals("=")){
			return false;
		}
		
		//both values mustn't be constant
		if (expression.getFirstExpression().isIsConstant() || expression.getSecondExpression().isIsConstant()){
			return false;
		}
		
		//needs to have attributes in both cross-partners
		if (!hasSharedAttributes(cross.getChild(), expression) || !hasSharedAttributes(cross.getSecondChild(), expression)){
			return false;
		}
		
		return true;
	}

	private boolean hasSharedAttributes(ITreeNode node, EqualityExpression expression) throws Exception{
		boolean sharedAttributes = false;
		for (String attribute : expression.getAttributes()){
			if (attribute.contains(".")){
				if (node.getAttributes().contains(attribute)){
					sharedAttributes = true;
				}
			} else {
				for (String crossAttribute : node.getAttributes()){
					if (crossAttribute.split("\\.")[1].equals(attribute)){
						sharedAttributes = true;
					}
				}
			}
		}
		return sharedAttributes;
	}
	
}
