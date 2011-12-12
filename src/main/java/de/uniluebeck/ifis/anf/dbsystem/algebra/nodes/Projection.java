package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author seidel
 */
public class Projection extends OneChildNode
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
  public Relation evaluate() throws Exception
  {
	adjustColumnNames();
    Relation relation = this.getChild().evaluate();
    Relation result = new Relation();
    result.setAlias(relation.getAlias());
    result.setName(relation.getName());
    result.setDrop(false);
    String[] columnNames = new String[this.columnNames.length];
    for (int i = 0; i < columnNames.length; ++i)
    {
      if (this.columnNames[i].contains("."))
      {
        columnNames[i] = this.columnNames[i];
      }
      else
      {
        System.out.println("Projection has no . in column name, this shouldn't be.");
      }
    }
    result.setColumnNames(columnNames);

    for (Row row: relation.getRows())
    {
      Row newRow = new Row();
      newRow.name = row.name;
      newRow.alias = row.alias;
      newRow.columnNames = result.getColumnNames();
      newRow.tuple = new String[this.columnNames.length];
      for (int i = 0; i < this.columnNames.length; ++i)
      {
        for (int j = 0; j < row.columnNames.length; ++j)
        {
          if (newRow.columnNames[i].equals(row.columnNames[j]))
          {
            newRow.tuple[i] = row.tuple[j];
          }
        }
      }
      result.getRows().add(newRow);
    }

    return removeDoubles(result);
  }

  private void adjustColumnNames() throws Exception{
	  List<String> newColumnNames = new ArrayList<String>();
	  List<String> childAttributes = this.getChild().getAttributes();
	  for (String childAttribute : childAttributes){
		  for (String columnName : this.getColumnNames()){
				if (childAttribute.equals(columnName)) {
					newColumnNames.add(childAttribute);
				} else if (!columnName.contains(".")
						&& childAttribute.split("\\.")[1].equals(columnName)) {
					newColumnNames.add(childAttribute);
				}
		  }
	  }
	  this.setColumnNames(newColumnNames.toArray(new String[0]));
  }
  
	private Relation removeDoubles(Relation relation) {
		for (int i = 0; i < relation.getRows().size(); i++) {
			for (int j = i + 1; j < relation.getRows().size(); j++) {
				boolean remove = true;
				for (int index = 0; index < relation.getRows().get(i)
						.getTuple().length; index++) {
					if (!relation.getRows().get(i).getTuple()[index].equals(relation
							.getRows().get(j).getTuple()[index])) {
						remove = false;
					}
				}
				if (remove) {
					relation.getRows().remove(j);
					j--;
				}
			}
		}
		return relation;
	}
  /**
   * Calculate costs for projection:
   * 
   * Rows(T) * n
   *
   * Where n is the number of projected columns.
   *
   * @return Costs of projection
   *
   * @throws Exception Error during evaluation
   */
  @Override
  public int getCosts() throws Exception
  {
    int oldCost = getChild().getCosts();
    return oldCost + this.getChild().evaluate().getRows().size() * this.columnNames.length;
  }
}
