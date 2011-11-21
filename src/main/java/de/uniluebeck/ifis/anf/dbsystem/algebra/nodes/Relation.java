package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author seidel
 */
public class Relation extends ITreeNode
{
  public static ITreeNode createSelection(List<String> columnNames, List<String> tableNames, AndExpression whereClause)
  {
    Projection projection = new Projection();
    projection.setColumnNames(columnNames.toArray(new String[0]));

    OneChildNode lowerNode = projection;

    if (whereClause != null)
    {
      Selection selection = new Selection();
      selection.setExpression(whereClause);
      projection.setChild(selection);
      lowerNode = selection;
    }

    for (int i = 0; i < tableNames.size() - 1; ++i)
    {
      CrossProduct cross = new CrossProduct();
      cross.setSecondChild(Table.loadTable(tableNames.get(i)).toRelation());
      lowerNode.setChild(cross);
      lowerNode = cross;
    }
    Table table = Table.loadTable(tableNames.get(tableNames.size() - 1));
    lowerNode.setChild(table.toRelation());

    return projection;
  }

  public static ITreeNode createSelection(List<String> columnNames, List<String> tableNames)
  {
    return createSelection(columnNames, tableNames, null);
  }
  
  private String name;

  private String alias;
  
  private boolean drop;

  private String[] columnNames;

  private ArrayList<Row> rows;

  public Relation()
  {
    this.rows = new ArrayList<Row>();
  }

  public String getAlias()
  {
    return alias;
  }

  public void setAlias(String alias)
  {
    this.alias = alias;
  }

  public String[] getColumnNames()
  {
    return columnNames;
  }

  public void setColumnNames(String[] columnNames)
  {
    this.columnNames = columnNames;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean isDropped()
  {
    return drop;
  }

  public void setDrop(boolean drop)
  {
    this.drop = drop;
  }

  public ArrayList<Row> getRows()
  {
    return rows;
  }

  public void setRows(ArrayList<Row> rows)
  {
    this.rows = rows;
  }

  @Override
  public Relation evaluate()
  {
    return this;
  }

  public Table toTable()
  {
    Table table = new Table(this.getName(), this.getAlias(), this.getColumnNames());
    table.setDrop(this.drop);

    for (Row row: this.getRows())
    {
      table.addRow(row.getTuple());
    }

    return table;
  }
}
