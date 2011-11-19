package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public abstract class ITreeNode
{
  abstract public Relation evaluate() throws Exception;

  public int getCosts() throws Exception
  {
    return 0;
  }
}
