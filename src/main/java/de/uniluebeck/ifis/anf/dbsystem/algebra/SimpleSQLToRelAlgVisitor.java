package de.uniluebeck.ifis.anf.dbsystem.algebra;

import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.parser.syntaxtree.*;
import de.uniluebeck.ifis.anf.dbsystem.parser.visitor.ObjectDepthFirst;
import java.util.ArrayList;
import java.util.List;

/**
 * @author seidel
 */
public class SimpleSQLToRelAlgVisitor extends ObjectDepthFirst
{
  private ITreeNode executionPlan;
  
  /**
   * Return execution plan.
   */
  public ITreeNode getExecutionPlan()
  {
    return executionPlan;
  }
  
   /**
    * f0 -> Query()
    *       | Update()
    *       | Delete()
    *       | Insert()
    *       | CreateTable()
    *       | DropTable()
    */
   public Object visit(CompilationUnit n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <SELECT>
    * f1 -> Items()
    * f2 -> <FROM>
    * f3 -> Tables()
    * f4 -> [ Where() ]
    */
   public Object visit(Query n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Item()
    * f1 -> ( "," Item() )*
    */
   public Object visit(Items n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Name()
    * f1 -> [ "." Name() ]
    */
   public Object visit(Item n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Table()
    * f1 -> ( "," Table() )*
    */
   public Object visit(Tables n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Name()
    * f1 -> [ <AS> Name() ]
    */
   public Object visit(Table n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <WHERE>
    * f1 -> AndExpression()
    */
   public Object visit(Where n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> OrExpression()
    * f1 -> ( <AND> OrExpression() )*
    */
   public Object visit(AndExpression n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> [ "(" ]
    * f1 -> EqualityExpression()
    * f2 -> ( <OR> EqualityExpression() )*
    * f3 -> [ ")" ]
    */
   public Object visit(OrExpression n, Object argu) {
     de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OrExpression expr =
             new de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.OrExpression();
     
     // TODO: Continue with implementation here
     
     return expr;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> [ ( "=" | "!=" | <LT> | <GT> | <LE> | <GE> ) PrimaryExpression() ]
    */
   public Object visit(EqualityExpression n, Object argu) {
     de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.EqualityExpression expr =
             new de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.EqualityExpression();
     
     // Set first expression
     expr.setFirstExpression((de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.PrimaryExpression)n.f0.accept(this, argu));
     
     // Set operator
     if (n.f1.present())
     {
       NodeSequence ns = (NodeSequence)n.f1.node;
       NodeChoice nc = (NodeChoice)ns.elementAt(0);
       switch (nc.which)
       {
         case 0:
           expr.setOperator("=");
           break;
         case 1:
           expr.setOperator("!=");
           break;
         case 2:
           expr.setOperator("<");
           break;
         case 3:
           expr.setOperator(">");
           break;
         case 4:
           expr.setOperator("<=");
           break;
         case 5:
           expr.setOperator(">=");
           break;
         default:
           throw new RuntimeException("Invalid operator in equality expression.");
       }
       
       expr.setSecondExpression((de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.PrimaryExpression)ns.elementAt(1).accept(this, argu));        
     }
     
     return expr;
   }

   /**
    * f0 -> <IDENTIFIER> [ "." <IDENTIFIER> ]
    *       | LiteralExpression()
    * 
    * argu = List of table columns
    */
   public Object visit(PrimaryExpression n, Object argu) {
     de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.PrimaryExpression expr =
             new de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.PrimaryExpression();
     
      List<String> tableColumns = (List<String>)argu;
      String name = "";
      switch (n.f0.which)
      {
        case 0:
          NodeSequence ns = (NodeSequence) n.f0.choice;
          name = ns.elementAt(0).toString();
          NodeOptional nodeOptional = (NodeOptional)ns.elementAt(1);
          if (nodeOptional.present())
          {
            name += "." + ((NodeSequence)nodeOptional.node).elementAt(1).toString();
          }
          break;
        case 1:
          LiteralExpression lit = (LiteralExpression)n.f0.choice;
          name = lit.f0.toString();
          break;
      }
      
      expr.setValue(name);
      expr.setIsConstant(!tableColumns.contains(name));
      
      return expr;
   }

   /**
    * f0 -> <STRING_LITERAL>
    * f1 -> ( "," <STRING_LITERAL> )*
    */
   public Object visit(Literals n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <UPDATE>
    * f1 -> Table()
    * f2 -> <SET>
    * f3 -> AssignExpressions()
    * f4 -> [ Where() ]
    */
   public Object visit(Update n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> AssignExpression()
    * f1 -> ( "," AssignExpression() )*
    */
   public Object visit(AssignExpressions n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Name()
    * f1 -> "="
    * f2 -> LiteralExpression()
    */
   public Object visit(AssignExpression n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <STRING_LITERAL>
    */
   public Object visit(LiteralExpression n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <DELETE>
    * f1 -> <FROM>
    * f2 -> Table()
    * f3 -> [ Where() ]
    */
   public Object visit(Delete n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <INSERT>
    * f1 -> <INTO>
    * f2 -> Table()
    * f3 -> [ "(" ColumnNames() ")" ]
    * f4 -> <VALUES>
    * f5 -> "("
    * f6 -> Literals()
    * f7 -> ")"
    */
   public Object visit(Insert n, Object argu) {
     de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.Insert insertOperation =
             new de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.Insert();
     
     // Set table name
     insertOperation.setName(n.f2.f0.f0.tokenImage);
     
     // Set column names
     List<String> columnNames = new ArrayList<String>();
     if (n.f3.present())
     {
       ColumnNames columnNamesFromTree = (ColumnNames)(n.f3.node);
               
       // Add first column name
       columnNames.add(columnNamesFromTree.f0.f0.toString());
       
       // Add other column names
       for (Object columnName: columnNamesFromTree.f1.nodes)
       {
         columnNames.add(((Name)columnName).f0.toString());
       }
       
       insertOperation.setColumnNames(columnNames.toArray(new String[0]));
     }
     
     // Set column values
     List<String> values = new ArrayList<String>();
     
     // Set first value
     values.add(n.f6.f0.toString());
     
     // Set other values
     for (Object nodeSeq: n.f6.f1.nodes)
     {
       NodeSequence nodeSeqObj = (NodeSequence)nodeSeq;
       NodeToken literal = (NodeToken)nodeSeqObj.elementAt(1);
       values.add(literal.toString());
     }
     
     insertOperation.setValues(values.toArray(new String[0]));
     
     this.executionPlan = insertOperation;
     
     return null;
   }

   /**
    * f0 -> Name()
    * f1 -> ( "," Name() )*
    */
   public Object visit(ColumnNames n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <CREATE>
    * f1 -> <TABLE>
    * f2 -> Name()
    * f3 -> "("
    * f4 -> ColumnDefinition()
    * f5 -> ( "," ColumnDefinition() )*
    * f6 -> ")"
    */
   public Object visit(CreateTable n, Object argu) {
     de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.CreateTable createTableOperation =
             new de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.CreateTable();
     
     // Set table name
     createTableOperation.setName(n.f2.f0.toString());
     
     // Get column names
     String[] columnNames = new String[n.f5.nodes.size() + 1];
     
     // First column definition
     columnNames[0] = n.f4.f0.f0.toString();
     
     // Get other column definitions
     for (int i = 0; i < n.f5.nodes.size(); ++i)
     {
       // Get name of column i
       columnNames[i + 1] = ((ColumnDefinition)(((NodeSequence)n.f5.nodes.get(i)).elementAt(1))).f0.f0.toString();
     }
     
     createTableOperation.setColumnNames(columnNames);
     
     this.executionPlan = createTableOperation;
     
     return null;
   }

   /**
    * f0 -> <DROP>
    * f1 -> Name()
    */
   public Object visit(DropTable n, Object argu) {
     de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.DropTable dropTableOperation =
             new de.uniluebeck.ifis.anf.dbsystem.algebra.tableOperations.DropTable();
     
     dropTableOperation.setName(n.f1.f0.toString()); // Name as string
     this.executionPlan = dropTableOperation;
     
     return null;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public Object visit(Name n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Name()
    * f1 -> DataType()
    */
   public Object visit(ColumnDefinition n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <VARCHAR>
    */
   public Object visit(DataType n, Object argu) {
      Object _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

}
