//
// Generated by JTB 1.2.2
//

package de.uniluebeck.ifis.anf.dbsystem.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> <CREATE>
 * f1 -> <TABLE>
 * f2 -> Name()
 * f3 -> "("
 * f4 -> ColumnDefinition()
 * f5 -> ( "," ColumnDefinition() )*
 * f6 -> ")"
 */
public class CreateTable implements Node {
   private Node parent;
   public NodeToken f0;
   public NodeToken f1;
   public Name f2;
   public NodeToken f3;
   public ColumnDefinition f4;
   public NodeListOptional f5;
   public NodeToken f6;

   public CreateTable(NodeToken n0, NodeToken n1, Name n2, NodeToken n3, ColumnDefinition n4, NodeListOptional n5, NodeToken n6) {
      f0 = n0;
      if ( f0 != null ) f0.setParent(this);
      f1 = n1;
      if ( f1 != null ) f1.setParent(this);
      f2 = n2;
      if ( f2 != null ) f2.setParent(this);
      f3 = n3;
      if ( f3 != null ) f3.setParent(this);
      f4 = n4;
      if ( f4 != null ) f4.setParent(this);
      f5 = n5;
      if ( f5 != null ) f5.setParent(this);
      f6 = n6;
      if ( f6 != null ) f6.setParent(this);
   }

   public CreateTable(Name n0, ColumnDefinition n1, NodeListOptional n2) {
      f0 = new NodeToken("create");
      if ( f0 != null ) f0.setParent(this);
      f1 = new NodeToken("table");
      if ( f1 != null ) f1.setParent(this);
      f2 = n0;
      if ( f2 != null ) f2.setParent(this);
      f3 = new NodeToken("(");
      if ( f3 != null ) f3.setParent(this);
      f4 = n1;
      if ( f4 != null ) f4.setParent(this);
      f5 = n2;
      if ( f5 != null ) f5.setParent(this);
      f6 = new NodeToken(")");
      if ( f6 != null ) f6.setParent(this);
   }

   public void accept(de.uniluebeck.ifis.anf.dbsystem.parser.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(de.uniluebeck.ifis.anf.dbsystem.parser.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
   public void setParent(Node n) { parent = n; }
   public Node getParent()       { return parent; }
}
