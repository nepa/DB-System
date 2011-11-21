package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author seidel
 */
public class Relation extends ITreeNode
{
	
  public static ITreeNode createSelection(List<String> columnNames, List<String[]> tableNames, AndExpression whereClause)
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
      Table table = Table.loadTable(tableNames.get(i)[0]);
      table.setAlias(tableNames.get(i)[1]);
      cross.setSecondChild(table.toRelation());
      lowerNode.setChild(cross);
      lowerNode = cross;
    }
    System.out.println("Name = " + tableNames.get(0)[0]);
    System.out.println("Alias = " + tableNames.get(0)[1]);
    
    
    Table table = Table.loadTable(tableNames.get(tableNames.size() - 1)[0]);
    table.setAlias(tableNames.get(tableNames.size() - 1)[1]);
    lowerNode.setChild(table.toRelation());

    return projection;
  }

  public static ITreeNode createSelection(List<String> columnNames, List<String[]> tableNames)
  {
    return createSelection(columnNames, tableNames, null);
  }
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

  @Override
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
  
  /**
   * check if the column with the given index has the given name, take into account
   * that a . can be in name but doesn't need to be
   * @param index
   * @param name
   * @return
   */
  public boolean columnNameEquals(int index, String name){
	  if (name.contains(".")){
		  return columnNames[index].equals(name);
	  } else {
		  return this.columnNames[index].split("\\.")[1].equals(name);
	  }
  }
}
