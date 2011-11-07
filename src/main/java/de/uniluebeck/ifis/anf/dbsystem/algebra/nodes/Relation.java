package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.ArrayList;

/**
 * @author seidel
 */
public class Relation implements ITreeNode
{
  private String name;

  private String alias;

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

  public Table toTable()
  {
    Table table = new Table(this.getName(), this.getAlias(), this.getColumnNames());

    for (Row row: this.getRows())
    {
      table.addRow(row.getTuple());
    }

    return table;
  }
}
