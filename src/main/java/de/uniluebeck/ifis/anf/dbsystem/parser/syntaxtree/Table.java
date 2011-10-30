//
// Generated by JTB 1.2.2
//

package de.uniluebeck.ifis.anf.dbsystem.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> Name()
 * f1 -> [ <AS> Name() ]
 */
public class Table implements Node {
   private Node parent;
   public Name f0;
   public NodeOptional f1;

   public Table(Name n0, NodeOptional n1) {
      f0 = n0;
      if ( f0 != null ) f0.setParent(this);
      f1 = n1;
      if ( f1 != null ) f1.setParent(this);
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

