package de.uniluebeck.ifis.anf.dbsystem.transaction;

import de.uniluebeck.ifis.anf.dbsystem.Application;
import de.uniluebeck.ifis.anf.dbsystem.algebra.nodes.ITreeNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author seidel
 */
public class ClientThread extends Thread
{
  private File commandFile;

  public ClientThread(final String fileName)
  {
    this.commandFile = new File(Application.QUERY_PATH + fileName);
  }

  @Override
  public void run()
  {
    Scanner inputFile = null;

    // Try to load data from file
    try
    {
      long tid = Scheduler.getInstance().beginTransaction();

      inputFile = new Scanner(this.commandFile);

      // Process all commands by reading file line by line
      while (null != inputFile && inputFile.hasNext())
      {
        // Get next line
        String line = inputFile.nextLine();

        // Check if line ends with semicolon
        if (line.length() > 0 && (";").equals(String.valueOf(line.charAt(line.length() - 1))))
        {
          // Query is line without semicolon
          String query = line.substring(0, line.length() - 1);

          // Execute command
          ITreeNode executionPlan = Application.sqlToRelationenAlgebra(query);

          TableAndCost tac = Scheduler.getInstance().execute(tid, executionPlan);
          System.out.println("TID " + tid + ": " + tac);
        }
      }

      Scheduler.getInstance().endTransaction(tid);
    }
    catch (FileNotFoundException e)
    {
      String fileName = (this.commandFile.getName());
      System.err.println(String.format("Could not load commands from file '%s'. It does not exist.", fileName));
    }
  }
}
