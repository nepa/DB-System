package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public abstract class TwoChildNode extends OneChildNode
{
  private ITreeNode secondChild;
  
  public ITreeNode getSecondChild()
  {
    return this.secondChild;
  }
  
  public void setSecondChild(ITreeNode secondChildNode)
  {
    this.secondChild = secondChildNode;
  }
}
