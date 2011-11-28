package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public abstract class ITreeNode
{
  abstract public Relation evaluate() throws Exception;

  private ITreeNode parent;
  
  public int getCosts() throws Exception
  {
    return 0;
  }
  
  public ITreeNode getParent(){
	  return parent;
  }
  
  public void setParent(ITreeNode parent){
	  this.parent = parent;
  }
}
