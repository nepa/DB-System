package de.uniluebeck.ifis.anf.dbsystem;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for DB-System application.
 */
public class ApplicationTest extends TestCase
{
  /**
   * Constructor creates the desired test case.
   *
   * @param testName Name of the test case
   */
  public ApplicationTest(String testName)
  {
    super(testName);
  }

  /**
   * Helper method that returns the test suite that should be run.
   *
   * @return Test suite to be run
   */
  public static Test suite()
  {
    return new TestSuite(ApplicationTest.class);
  }

  /**
   * Unit test for DB-System application.
   */
  public void testApp()
  {
    assertTrue(true); // TODO: Implement unit test
  }
}
