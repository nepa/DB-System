package de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;

/**
 * @author seidel
 */
public class DropTable extends TableOperation
{
  @Override
  public Table execute()
  {
    Table table = Table.loadTable(this.name);
    table.setDrop(true);

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
  
  @Override
  public Relation evaluate()
  {
    return this.execute().toRelation();
  }
}
