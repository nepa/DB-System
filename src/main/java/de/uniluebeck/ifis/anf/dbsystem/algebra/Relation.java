package de.uniluebeck.ifis.anf.dbsystem.algebra;

import java.util.ArrayList;

/**
 * @author seidel
 */
public class Relation implements ITreeNode
{
  private ArrayList<Row> rows;
  
  public Relation()
  {
    this.rows = new ArrayList<Row>();
  }

  public ArrayList<Row> getRows()
  {
    return rows;
  }

  public void setRows(ArrayList<Row> rows)
  {
    this.rows = rows;
  }

  public Relation evaluate()
  {
    return this;
  }
}
