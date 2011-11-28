package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

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
    secondChildNode.setParent(this);
  }
  
  @Override
  public int getCosts() throws Exception
  {
    return this.getChild().getCosts() + this.getSecondChild().getCosts();
  }
}
