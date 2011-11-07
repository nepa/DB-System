package de.uniluebeck.ifis.anf.dbsystem.algebra.nodes;

/**
 * @author seidel
 */
public class CrossProduct extends TwoChildNode
{

  public Relation evaluate()
  {
    Relation rel1 = getChild().evaluate();
    Relation rel2 = getSecondChild().evaluate();
    
    Relation result = new Relation();
    for (Row row1 : rel1.getRows()){
      for (Row row2 : rel2.getRows()){
        Row newRow = new Row();
        newRow.name = row1.name + "x" + row2.name;
        newRow.alias = row1.alias + "x" + row2.alias;
        newRow.tuple = new String[row1.tuple.length + row2.tuple.length];
        newRow.tupleNames = new String[row1.tupleNames.length + row2.tupleNames.length];
        for (int i = 0; i < row1.tupleNames.length; ++i){
          newRow.tuple[i] = row1.tuple[i];
          newRow.tupleNames[i] = row1.tupleNames[i];
        }
        for (int i = 0; i < row2.tupleNames.length; ++i){
          newRow.tuple[i + row1.tupleNames.length] = row2.tuple[i];
          newRow.tupleNames[i + row1.tupleNames.length] = row2.tupleNames[i];
        }
        result.getRows().add(newRow);
      }
    }
    return result;
  }
}
