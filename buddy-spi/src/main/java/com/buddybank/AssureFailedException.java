package com.buddybank;

/**
 * Programmers exception indicating a condition that involve an invalid application state were met. 
 * 
 * @author Ivan Saorin (US01889)
 * @version 1.0
 * @since 1.0							
 * @see com.buddybank.union.Assure
 */

public class AssureFailedException extends RuntimeException
{
  private static final long serialVersionUID = 2472643475637784069L;


  public AssureFailedException()
  {
    super();
  }


  public AssureFailedException(String message, Throwable cause)
  {
    super(message, cause);
  }


  public AssureFailedException(String message)
  {
    super(message);
  }


  public AssureFailedException(Throwable cause)
  {
    super(cause);
  }
}
