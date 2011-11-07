package de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.AndExpression;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Row;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Selection;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;
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
    Table table = Table.loadTable(this.name);
    
    Relation relation = table.toRelation();
    List<Row> rows = this.getRelevantRows(relation);
    relation.getRows().removeAll(rows);
    
    table = relation.toTable();
    
    try
    {
      table.write();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return table;
  }
  
  protected List<Row> getRelevantRows(Relation relation){
    Selection selection = new Selection();
    selection.setChild(relation);
    selection.setExpression(whereClause);
    return selection.evaluate().getRows();
  }
    
  @Override
  public Relation evaluate()
  {
    return this.execute().toRelation();
  }
}
