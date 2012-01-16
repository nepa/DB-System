package de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;

/**
 * @author seidel
 */
public class DropTable extends TableOperation
{
  @Override
  public Table execute() throws Exception
  {    
    Table table = Table.loadTable(this.name);
    table.setDrop(true);
    
//     table.write(); // TODO: Remove
    
    return table;
  }  
  
  @Override
  public Relation evaluate() throws Exception
  {
    return this.execute().toRelation();
  }
}
