package de.uniluebeck.ifis.anf.dbsystem.transaction;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;

/**
 * Interface for transaction scheduler.
 * 
 * @author seidel
 */
public interface IScheduler
{
  public long beginTransaction();
  
  public void endTransaction(final long tid);
  
  public void abortTransaction(final long tid);
  
  public TableAndCost execute(final long tid, final ITreeNode plan);
  
  public TableAndCost execute(final ITreeNode plan);
}
