package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author seidel
 */
public abstract class ITreeNode
{
  abstract public Relation evaluate() throws Exception;

  private OneChildNode parent;
  
  public int getCosts() throws Exception
  {
    return 0;
  }
  
  public OneChildNode getParent(){
	  return parent;
  }
  
  public void setParent(OneChildNode parent){
	  this.parent = parent;
  }
  
  public List<String> getAttributes() throws Exception{
	  Relation relation = this.evaluate();
	  List<String> result = new ArrayList<String>();
	  for (String column : relation.getColumnNames()){
		  result.add(column);
	  }
	  return result;
  }
}
