package de.uniluebeck.ifis.anf.dbsystem.optimierung;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.AndExpression;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OneChildNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Selection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OrExpression;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.TwoChildNode;

/**
 * @author seidel
 */
public class CascadeSelects implements IOptimization {

	@Override
	public ITreeNode optimize(ITreeNode executionPlan) {
		if (executionPlan.getClass() == Selection.class) {
			boolean first = true;
			Selection selection = (Selection) executionPlan;
			OneChildNode parent = selection.getParent();
			ITreeNode child = selection.getChild();
			for (OrExpression orExpression : selection.getExpression().getExpressions()) {			
				Selection newSelection = new Selection(new AndExpression(orExpression));
				if (first) {
					executionPlan = newSelection;
					first = false;
				}
				if (parent != null){
					if (parent.getClass() == TwoChildNode.class && parent.getChild() != selection){
						TwoChildNode twoChild = (TwoChildNode) parent;
						twoChild.setSecondChild(newSelection);
					} else {
						parent.setChild(newSelection);
					}
				}
				parent = newSelection;
			}
			if (parent != null){
				parent.setChild(child);
			}
		}
		
		// Recursion
		if (executionPlan instanceof OneChildNode){
			OneChildNode oneChild = (OneChildNode) executionPlan;
			oneChild.setChild(optimize(oneChild.getChild()));
		}
		if (executionPlan instanceof TwoChildNode){
			// First child is optimized since TwoChildNode extends OneChildNode
			TwoChildNode twoChild = (TwoChildNode) executionPlan;
			twoChild.setSecondChild(optimize(twoChild.getSecondChild()));
		}
		return executionPlan;
	}
}
