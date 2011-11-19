package de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;

/**
 * @author seidel
 */
public class CreateTable extends TableOperation
{
  protected String[] columnNames;

  public String[] getColumnNames()
  {
    return columnNames;
  }

  public void setColumnNames(String[] columnNames)
  {
    this.columnNames = columnNames;
  }

  @Override
  public Table execute() throws Exception
  {
    Table table = new Table(this.name, this.name, this.columnNames);
    table.write();

    return table;
  }

  @Override
  public Relation evaluate() throws Exception
  {
    return this.execute().toRelation();
  }
}
