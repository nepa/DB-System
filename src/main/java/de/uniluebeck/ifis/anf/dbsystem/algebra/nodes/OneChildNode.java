package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public abstract class OneChildNode implements ITreeNode
{
  private ITreeNode childNode;
  
  public ITreeNode getChild()
  {
    return this.childNode;
  }
  
  public void setChild(ITreeNode childNode)
  {
    this.childNode = childNode;
  }
}
