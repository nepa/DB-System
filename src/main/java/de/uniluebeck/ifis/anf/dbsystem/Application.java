package de.uniluebeck.ifis.anf.dbsystem;

import de.uniluebeck.ifis.anf.dbsystem.algebra.ITreeNode;

/**
 * DB-System main application.
 */
public class Application
{
  /** Path to customer database */
  public static final String KUNDENDB = "";

  /**
   * Application program to run DB-System application.
   *
   * @param args Array of commandline arguments
   */
  public static void main(String[] args)
  {
    // TODO: Add tests here?
  }

  public static void printKundenDB()
  {
    // TODO
  }

  public static void createKundenDB()
  {
    // TODO
  }

  public static void execute(String simpleSQL)
  {
    // TODO: Translate requests
    // TODO: Execute requests
  }

  public static ITreeNode sqlToRelationenAlgebra(String simpleSQL)
  {
    // TODO

    return null;
  }

  private static void executePlan(ITreeNode plan)
  {
    // TODO
  }
}
