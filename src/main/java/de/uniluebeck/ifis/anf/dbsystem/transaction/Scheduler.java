package de.uniluebeck.ifis.anf.dbsystem.transaction;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OneChildNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.TwoChildNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.TableOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the transaction scheduler.
 * @author seidel
 */
public class Scheduler implements IScheduler
{  
  private static Scheduler instance;
  
  private long currentTransactionID;
  
  private Map<Long, List<Table>> workspaces;
  private Map<Long, List<String>> writtenTables;
  private Map<Long, Long> startingTime;
  
  /**
   * Singleton implementation, use getInstance().
   */
  private Scheduler()
  {
    currentTransactionID = 0;
    
    workspaces = new HashMap<Long, List<Table>>();
    writtenTables = new HashMap<Long, List<String>>();
    startingTime = new HashMap<Long, Long>();
  }
  
  public static synchronized Scheduler getInstance()
  {
    if (instance == null)
    {
      instance = new Scheduler();
    }
    
    return instance;
  }

  @Override
  public synchronized long beginTransaction()
  {
    long newID = currentTransactionID++;
    workspaces.put(newID, new ArrayList<Table>());
    writtenTables.put(newID, new ArrayList<String>());
    startingTime.put(newID, System.currentTimeMillis());
    return newID;
  }

  @Override
  public TableAndCost execute(long tid, ITreeNode plan)
  {
    //Executions on Transactions that haven't started yet, are not allowed
    if (!workspaces.containsKey(tid)){
      System.out.println("Aborting transaction with ID " + tid);
      return null;
    }
    
    System.out.println("Executing transaction with ID " + tid);
    
    TableAndCost result = null;
    try
    {
      updateWorkspace(getRelevantTables(plan), tid);
      ITreeNode updatedPlan = this.changePlan(plan, tid);
      Relation planResult = updatedPlan.evaluate();
      
      //update the workspace if something is changed
      if (updatedPlan instanceof TableOperation){
        Table newTable = planResult.toTable();
        List<Table> tables = workspaces.get(tid);
        Table deleteThis = null;
        for (Table table : tables){
          if (table.getName().equals(newTable.getName())){
            deleteThis = table;
          }
        }
        if (deleteThis != null){
          tables.remove(deleteThis);
        }
        tables.add(newTable);
        
        //update writtenTables
        if (!writtenTables.get(tid).contains(newTable.getName())){
          writtenTables.get(tid).add(newTable.getName());
        }
      }
      
      result = new TableAndCost();
      result.setTable(planResult.toTable());
      result.setCosts(plan.getCosts());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return result;
  }

  @Override
  public TableAndCost execute(ITreeNode plan)
  {
    TableAndCost result = null;
    
    long id = beginTransaction();
    result = execute(id, plan);
    endTransaction(id);
    
    return result;
  }

  @Override
  public synchronized void endTransaction(long tid)
  {
    List<Table> tables = workspaces.get(tid);
    boolean abort = false;
    long startTime = startingTime.get(tid);
    for (Table table: tables){
      Table loadedTable = Table.loadTable(table.getName());
      if (loadedTable.getLastWritten() > startTime){
        abort = true;
      }
    }
    
    if (abort){
      abortTransaction(tid);
    } else {
      List<String> writtenTables = this.writtenTables.get(tid);
      for (Table table : tables){
        if (writtenTables.contains(table.getName())){
          try
          {
            table.write();
          }
          catch (Exception ex)
          {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
      
      cleanUp(tid);
    }
  }

  @Override
  public void abortTransaction(long tid)
  {
    cleanUp(tid);
  }
  
  private ITreeNode changePlan(ITreeNode plan, long tid){
    if (plan instanceof OneChildNode){
      OneChildNode oneChild = (OneChildNode)plan;
      oneChild.setChild(changePlan(oneChild.getChild(), tid));
    }
    if (plan instanceof TwoChildNode){
      TwoChildNode twoChild = (TwoChildNode) plan;
      twoChild.setSecondChild(changePlan(twoChild.getSecondChild(), tid));
    }
    if (plan instanceof Relation){
      Relation rel = (Relation)plan;
      List<Table> tables = workspaces.get(tid);
      for (Table table : tables){
        if (table.getName().equals(rel.getName())){
          return table.toRelation();
        }
      }
    }
    return plan;
  }
  
  private List<String> getRelevantTables(ITreeNode plan){
    List<String> result = new LinkedList<String>();
    if (plan instanceof OneChildNode){
      OneChildNode oneChild = (OneChildNode)plan;
      result.addAll(getRelevantTables(oneChild.getChild()));
    }
    if (plan instanceof TwoChildNode){
      TwoChildNode twoChild = (TwoChildNode) plan;
      result.addAll(getRelevantTables(twoChild.getSecondChild()));
    }
    if (plan instanceof Relation){
      Relation rel = (Relation)plan;
      result.add(rel.getName());
    } else if (plan instanceof TableOperation){
      TableOperation tableOp = (TableOperation)plan;
      result.add(tableOp.getName());
    }
    return result;
  }
  
  private void updateWorkspace(List<String> relevantTables, long tid){
    List<Table> tables = workspaces.get(tid);
    for (String relevantTableName : relevantTables){
      boolean alreadyThere = false;
      for (Table table : tables){
        if (table.getName().equals(relevantTableName)){
          alreadyThere = true;
        }
      }
      if (!alreadyThere){
        Table addThis = Table.loadTable(relevantTableName);
        if (addThis != null){
          tables.add(addThis);
        }
      }
    }
  }
  
  private void cleanUp(long tid){
    workspaces.remove(tid);
    startingTime.remove(tid);
    writtenTables.remove(tid);
  }
}
