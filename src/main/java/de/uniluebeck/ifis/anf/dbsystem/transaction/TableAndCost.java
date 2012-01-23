package de.uniluebeck.ifis.anf.dbsystem.transaction;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;

/**
 * Container class for result table object and query execution costs.
 * 
 * @author seidel
 */
public class TableAndCost
{
  private Table table;
  
  private int costs;

  public int getCosts()
  {
    return costs;
  }

  public void setCosts(int costs)
  {
    this.costs = costs;
  }

  public Table getTable()
  {
    return table;
  }

  public void setTable(Table table)
  {
    this.table = table;
  }
  
  public String toString()
  {
    return "Costs for Table " + table.getName() + ": " + costs + "\n\n" + table.toString();
  }
}
