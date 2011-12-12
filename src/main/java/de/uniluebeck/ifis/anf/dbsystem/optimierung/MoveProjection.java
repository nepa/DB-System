package de.uniluebeck.ifis.anf.dbsystem.optimierung;

import java.util.ArrayList;
import java.util.List;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.AndExpression;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.CrossProduct;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Join;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OneChildNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Projection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Selection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.TwoChildNode;

public class MoveProjection implements IOptimization {

	@Override
	public ITreeNode optimize(ITreeNode executionPlan) throws Exception {
		if (executionPlan.getClass() == Projection.class){
			Projection proj = (Projection) executionPlan;
			ITreeNode child = proj.getChild();
			OneChildNode parent = proj.getParent();
			boolean secondChild = (parent == null) ? false : (parent.getChild() != proj);
			
			//make sure every entry in columnNames is in the form alias.columnname
			proj.setColumnNames(proj.getAttributes().toArray(new String[0]));
			
			//remove projections below this one
			while (child.getClass() == Projection.class){
				Projection childProj = (Projection) child;
				proj.setChild(childProj.getChild());
				child = proj.getChild();
			}
			
			if (child.getClass() == Selection.class){
				Selection sel = (Selection) child;
				
				//if swappable, swap
				if (isMovable(sel.getExpression(), proj)){
					proj.setChild(sel.getChild());
					if (parent != null){
						if (secondChild){
							TwoChildNode twoChild = (TwoChildNode) parent;
							twoChild.setSecondChild(sel);
						} else {
							parent.setChild(sel);
						}
					} else {
						sel.setParent(null);
					}
					executionPlan = sel;
				}
			}
			
			if (child instanceof CrossProduct){
				CrossProduct cross = (CrossProduct) child;
				
				//determine if swappable
				boolean changeable = true;
				if (cross instanceof Join){
					Join join = (Join) cross;
					changeable = isMovable(join.getExpression(), proj);
				}
				
				if (changeable){
					//create child projections
					Projection firstChildProj = createChildProjection(cross.getChild(), proj);
					Projection secondChildProj = createChildProjection(cross.getSecondChild(), proj);
					
					//remove old projection
					if (parent != null){
						if (secondChild){
							TwoChildNode twoChild = (TwoChildNode) parent;
							twoChild.setSecondChild(cross);
						} else {
							parent.setChild(cross);
						}
					} else {
						cross.setParent(null);
					}
					executionPlan = cross;
					
					//add child projections
					firstChildProj.setChild(cross.getChild());
					secondChildProj.setChild(cross.getSecondChild());
					cross.setChild(firstChildProj);
					cross.setSecondChild(secondChildProj);
				}
			}
		}
		
		//Full recursive action!
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
	
	private Projection createChildProjection(ITreeNode child, Projection proj) throws Exception{
		Projection result = new Projection();
		List<String> columnNames = new ArrayList<String>();
		for (String columnName : proj.getAttributes()){
			if (child.getAttributes().contains(columnName)){
				columnNames.add(columnName);
			}
		}
		result.setColumnNames(columnNames.toArray(new String[0]));
		return result;
	}

	
	private boolean isMovable(AndExpression expr, Projection proj) throws Exception{
		boolean changeable = true; 
		for (String expressionValue : expr.getAttributes()){
			if (!(proj.getAttributes().contains(expressionValue))){
				boolean found = false;
				for (String projValue : proj.getAttributes()){
					if (projValue.split("\\.")[1].equals(expressionValue)){
						found = true;
					}
				}
				if (!found){
					changeable = false;
				}
			}
		}
		return changeable;
	}
}
