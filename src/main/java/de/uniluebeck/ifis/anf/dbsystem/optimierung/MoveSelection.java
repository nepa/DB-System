package de.uniluebeck.ifis.anf.dbsystem.optimierung;

import java.util.ArrayList;
import java.util.List;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.CrossProduct;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Join;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OneChildNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Projection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Selection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.TwoChildNode;

public class MoveSelection implements IOptimization {

	@Override
	public ITreeNode optimize(ITreeNode executionPlan) throws Exception {
		List<Selection> selections = collectSelections(
				new ArrayList<Selection>(), executionPlan);
		for (Selection selection : selections) {
			while (isMovable(selection)) {
				if (selection.getChild() instanceof TwoChildNode){
					TwoChildNode twoChild = (TwoChildNode) selection.getChild();
					OneChildNode parent = selection.getParent();
					boolean secondChild = parent != null ? (parent.getChild() != selection) : false;
					if (allInside(selection, twoChild.getChild())){
						selection.setChild(twoChild.getChild());
						twoChild.setChild(selection);
					} else {
						selection.setChild(twoChild.getSecondChild());
						twoChild.setSecondChild(selection);
					}
					if (parent == null){
						executionPlan = twoChild;
						twoChild.setParent(null);
					} else {
						if (!secondChild){
							parent.setChild(twoChild);
						} else {
							((TwoChildNode) parent).setSecondChild(twoChild);
						}
					}
				} else if (selection.getChild() instanceof OneChildNode){
					OneChildNode oneChild = (OneChildNode) selection.getChild();
					OneChildNode parent = selection.getParent();
					boolean secondChild = parent != null ? (parent.getChild() != selection) : false;
					
					selection.setChild(oneChild.getChild());
					oneChild.setChild(selection);
					if (parent == null){
						executionPlan = oneChild;
						oneChild.setParent(null);
					} else {
						if (!secondChild){
							parent.setChild(oneChild);
						} else {
							((TwoChildNode) parent).setSecondChild(oneChild);
						}
					}
				}
			}
		}
		return executionPlan;
	}

	/**
	 * Find all selections inside the tree recursively
	 * 
	 * @param listSoFar
	 *            selections found so far
	 * @param current
	 *            the current node
	 * @return the list of all selection nodes
	 */
	private List<Selection> collectSelections(List<Selection> listSoFar,
			ITreeNode current) {
		if (current.getClass() == Selection.class) {
			listSoFar.add((Selection) current);
		}
		if (current instanceof OneChildNode) {
			OneChildNode oneChild = (OneChildNode) current;
			listSoFar = collectSelections(listSoFar, oneChild.getChild());
		}
		if (current instanceof TwoChildNode) {
			TwoChildNode twoChild = (TwoChildNode) current;
			listSoFar = collectSelections(listSoFar, twoChild.getSecondChild());
		}
		return listSoFar;
	}
	
	/**
	 * Decide if a given selection can be moved downwards
	 * 
	 * @param selection
	 * @return
	 * @throws Exception
	 */
	private boolean isMovable(Selection selection) throws Exception {
		if (selection.getChild().getClass() == Selection.class) {
			return true;
		}
		if (selection.getChild().getClass() == Projection.class) {
			return true;
		}
		if (selection.getChild().getClass() == CrossProduct.class
				|| selection.getChild().getClass() == Join.class) {
			CrossProduct cross = (CrossProduct) selection.getChild();
			if (allInside(selection, cross.getChild())
					|| allInside(selection, cross.getSecondChild())) {
				return true;
			}
		}
		// if child is a relation or no single child of a cross fits,
		// it won't work
		return false;
	}

	/**
	 * decide if all attributes of the selection are also in the node
	 * @param selection
	 * @param node
	 * @return
	 * @throws Exception 
	 */
	private boolean allInside(Selection selection, ITreeNode node) throws Exception{
		List<String> nodeAttributes = node.getAttributes();
		for (String exprAttribute : selection.getExpression().getAttributes()){
			boolean result = false;
			for (String nodeAttribute : nodeAttributes){
				if (exprAttribute.equals(nodeAttribute) || nodeAttribute.split("\\.")[1].equals(exprAttribute)){
					result = true;
				}
			}
			if (!result){
				return false;
			}
		}
		return true;
	}
	
}
