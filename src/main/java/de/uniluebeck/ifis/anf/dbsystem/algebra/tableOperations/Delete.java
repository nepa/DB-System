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
  public Table execute() throws Exception
  {
    Table table = Table.loadTable(this.name);

    Relation relation = table.toRelation();
    List<Row> rows = this.getRelevantRows(relation);
    relation.getRows().removeAll(rows);

    table = relation.toTable();
    table.write();

    return table;
  }

  protected List<Row> getRelevantRows(Relation relation) throws Exception
  {
    if (whereClause != null)
    {
      Selection selection = new Selection();
      selection.setChild(relation);
      selection.setExpression(whereClause);
      return selection.evaluate().getRows();
    }
    else
    {
      return relation.getRows();
    }
  }

  @Override
  public Relation evaluate() throws Exception
  {
    return this.execute().toRelation();
  }
}
