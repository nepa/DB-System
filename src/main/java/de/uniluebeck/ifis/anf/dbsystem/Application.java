package de.uniluebeck.ifis.anf.dbsystem;

import java.io.StringReader;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Relation;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.Table;
import de.uniluebeck.ifis.anf.dbsystem.parser.gene.ParseException;
import de.uniluebeck.ifis.anf.dbsystem.parser.gene.SimpleSQLParser;
import de.uniluebeck.ifis.anf.dbsystem.parser.syntaxtree.CompilationUnit;
import de.uniluebeck.ifis.anf.dbsystem.algebra.SimpleSQLToRelAlgVisitor;

/**
 * DB-System main application.
 */
public class Application
{
  /**
   * Application program to run DB-System application.
   *
   * @param args Array of commandline arguments
   */
  public static void main(String[] args)
  {
    // TODO: Add tests here?
    Application.createKundenDB();
    Application.executeQuery("select ID, Vorname from Kunde where ID = \"Kunde1\" or Vorname = \"KVorname2\"");
  }

  /**
   * Execute a SimpleSQL query on the database and print result.
   * 
   * @param query SimpleSQL query
   * 
   * @return Resulting table object
   */
  public static Table executeQuery(final String query)
  {
    // Translate SimpleSQL query to execution plan
    ITreeNode executionPlan = Application.sqlToRelationenAlgebra(query);

    // Execute execution plan
    Relation result = Application.executePlan(executionPlan);
    Application.printTable(result.toTable()); // TODO: Remove after debugging

    return result.toTable();
  }

  /**
   * Translate a SimpleSQL query to an ITreeNode object that
   * contains an execution plan.
   * 
   * @param query SimpleSQL query
   * 
   * @return Execution plan as ITreeNode object
   */
  public static ITreeNode sqlToRelationenAlgebra(final String query)
  {
    ITreeNode executionPlan = null;

    // Parse SimpleSQL query
    SimpleSQLParser parser = new SimpleSQLParser(new StringReader(query));
    parser.setDebugALL(false);

    try
    {
      CompilationUnit compUnit = parser.CompilationUnit();
      SimpleSQLToRelAlgVisitor visitor = new SimpleSQLToRelAlgVisitor();
      compUnit.accept(visitor, null);
      executionPlan = visitor.getExecutionPlan();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }

    return executionPlan;
  }

  /**
   * Evaluate an execution plan and return Relation object as result.
   *
   * @param executionPlan Execution plan for evaluation
   *
   * @return Resulting Relation object
   */
  private static Relation executePlan(final ITreeNode executionPlan)
  {
    return executionPlan.evaluate();
  }

  /**
   * Private helper method to print Table object.
   */
  public static void printTable(final Table table)
  {
    System.out.println("\n\n" + table + "\n\n");
  }

  /**
   * Private helper method to initially create and fill KundenDB.
   */
  public static void createKundenDB()
  {
    Application.executeQuery("create table Buch (ID varchar , Titel varchar);");
    Application.executeQuery("create table Kunde (ID varchar, Name varchar, Vorname varchar, Adresse varchar);");
    Application.executeQuery("create table Bestellung (ID varchar, Datum varchar, Preis varchar, IstBezahlt varchar);");
    Application.executeQuery("create table Buch_Autor (B_ID varchar, Autorenname varchar);");
    Application.executeQuery("create table Kunde_Bestellung (K_ID varchar, B_ID varchar);");
    Application.executeQuery("create table Buch_Bestellung (Bu_ID varchar, Be_ID varchar);");

    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch1\",\"Grundlagen von Datenbanksystemen\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch2\",\"Kennedys Hirn\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch3\",\"Die rote Antilope\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch4\",\"Einführung in die Automatentheorie, Formale Sprachen und Komplexitätstheorie\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch5\",\"Java ist auch eine Insel\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch6\",\"Verteilte Systeme. Grundlagen und Paradigmen\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch7\",\"Der Schwarm\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch8\",\"Computernetzwerke\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch9\",\"Die Gehilfin\");");
    Application.executeQuery("insert into Buch (ID,Titel) values (\"Buch10\",\"Tiefe\");");

    Application.executeQuery("insert into Kunde (ID,Name,Vorname,Adresse) values (\"Kunde1\",\"KName1\",\"KVorname1\",\"KAdresse1\");");
    Application.executeQuery("insert into Kunde (ID,Name,Vorname,Adresse) values (\"Kunde2\",\"KName2\",\"KVorname2\",\"KAdresse2\");");
    Application.executeQuery("insert into Kunde (ID,Name,Vorname,Adresse) values (\"Kunde3\",\"KName3\",\"KVorname3\",\"KAdresse3\");");
    Application.executeQuery("insert into Kunde (ID,Name,Vorname,Adresse) values (\"Kunde4\",\"KName4\",\"KVorname4\",\"KAdresse4\");");
    Application.executeQuery("insert into Kunde (ID,Name,Vorname,Adresse) values (\"Kunde5\",\"KName5\",\"KVorname5\",\"KAdresse5\");");

    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung1\",\"Datum1\",\"Preis1\",\"ja\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung2\",\"Datum2\",\"Preis2\",\"nein\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung3\",\"Datum3\",\"Preis3\",\"ja\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung4\",\"Datum4\",\"Preis4\",\"nein\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung5\",\"Datum5\",\"Preis5\",\"ja\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung6\",\"Datum6\",\"Preis6\",\"ja\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung7\",\"Datum7\",\"Preis7\",\"nein\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung8\",\"Datum8\",\"Preis8\",\"ja\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung9\",\"Datum9\",\"Preis9\",\"ja\");");
    Application.executeQuery("insert into Bestellung (ID,Datum,Preis,IstBezahlt) values (\"Bestellung10\",\"Datum10\",\"Preis10\",\"ja\");");

    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch1\",\"Ramez Elmasri\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch1\",\"Shamkant B. Navathe\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch2\",\"Henning Mankell\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch3\",\"Henning Mankell\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch4\",\"John E. Hopcroft\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch4\",\"Rajeev Motwani\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch4\",\"Jeffrey D. Ullman\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch5\",\"Christian Ullenboom\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch7\",\"Frank Schützing\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch6\",\"Andrew S. Tanenbaum\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch6\",\"Maarten van Steen\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch8\",\"Andrew S. Tanenbaum\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch9\",\"Martin Kluger\");");
    Application.executeQuery("insert into Buch_Autor (B_ID,Autorenname) values (\"Buch10\",\"Henning Mankell\");");

    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde1\",\"Bestellung1\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde1\",\"Bestellung2\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde1\",\"Bestellung3\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde2\",\"Bestellung4\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde2\",\"Bestellung5\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde3\",\"Bestellung6\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde4\",\"Bestellung7\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde5\",\"Bestellung8\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde5\",\"Bestellung9\");");
    Application.executeQuery("insert into Kunde_Bestellung (K_ID,B_ID) values (\"Kunde5\",\"Bestellung10\");");

    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch1\",\"Bestellung1\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch9\",\"Bestellung1\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch3\",\"Bestellung1\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch2\",\"Bestellung2\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch3\",\"Bestellung3\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch4\",\"Bestellung4\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch10\",\"Bestellung4\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch4\",\"Bestellung5\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch1\",\"Bestellung5\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch5\",\"Bestellung6\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch6\",\"Bestellung7\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch8\",\"Bestellung7\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch7\",\"Bestellung8\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch8\",\"Bestellung9\");");
    Application.executeQuery("insert into Buch_Bestellung (Bu_ID,Be_ID) values (\"Buch9\",\"Bestellung10\");");
  }
}
