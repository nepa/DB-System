package de.uniluebeck.ifis.anf.dbsystem.optimierung;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;

/**
 * Public interface for query optimizations.
 * 
 * @author seidel
 */
public interface IOptimization
{
  public ITreeNode optimize(ITreeNode executionPlan) throws Exception;
}
