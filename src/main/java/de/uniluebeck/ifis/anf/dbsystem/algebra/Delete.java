package de.uniluebeck.ifis.anf.dbsystem.algebra;

import java.util.List;

/**
 * @author seidel
 */
public class Delete extends TableOperation
{
  protected AndExpression whereClause;

  public AndExpression getWhereClause()
  {
    return whereClause;
  }

  public void setWhereClause(AndExpression whereClause)
  {
    this.whereClause = whereClause;
  }

  @Override
  public Table execute()
  {
    // TODO Delete relevant rows from table
    return null;
  }
  
  protected List<Row> getRelevantRows(Relation relation){
    Selection selection = new Selection();
    selection.setChild(relation);
    selection.setExpression(whereClause);
    return selection.evaluate().getRows();
  }
}
