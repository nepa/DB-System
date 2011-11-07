package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

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

  public Relation evaluate()
  {
    Relation relation = this.getChild().evaluate();
    
    Relation result = new Relation();    
    for (Row row: relation.getRows())
    {
      Row newRow = new Row();
      newRow.name = row.name;
      newRow.alias = row.alias;
      newRow.tupleNames = this.columnNames;
      newRow.tuple = new String[this.columnNames.length];
      for (int i = 0; i < this.columnNames.length; ++i){
        for (int j = 0; j < row.tupleNames.length; ++j){
          if (newRow.tupleNames[i].equals(row.tupleNames[j])){
            newRow.tuple[i] = row.tuple[j];
          }
        }
      }
    }
    
    return result;
  }
}
