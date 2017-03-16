package com.buddybank;

/**
 * Programmers exception indicating a condition that should not occurs were met. 
 * 
 * @author Ivan Saorin (US01889)
 * @version 1.0
 * @since 1.0							
 * @see com.buddybank.Assert
 */
public class AssertionFailedException extends RuntimeException
{
  private static final long serialVersionUID = 2472643475637784069L;


  public AssertionFailedException()
  {
    super();
  }


  public AssertionFailedException(String message, Throwable cause)
  {
    super(message, cause);
  }


  public AssertionFailedException(String message)
  {
    super(message);
  }


  public AssertionFailedException(Throwable cause)
  {
    super(cause);
  }
}
