package de.uniluebeck.ifis.anf.dbsystem.algebra;

/**
 * @author seidel
 */
public class DropTable extends TableOperation
{
  @Override
  public Table execute()
  {
    Table table = Table.loadTable(this.name);
    table.drop = true;

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
}
