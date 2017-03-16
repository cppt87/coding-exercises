package com.buddybank.utils;

/**
 * Extension of standard StringTokenizer exposing the tokens as an Array for ease of use. 
 * 
 * @author Ivan Saorin (US01889)
 * @version 1.0
 * @since 1.0							
 * @see java.util.StringTokenizer
 */

public class StringTokenizer extends java.util.StringTokenizer
{

  public static String[] tokenize(String str, String delim, boolean returnDelims) {
    return new StringTokenizer(str, delim, returnDelims).getTokens();
  }

  public static String[] tokenize(String str, String delim) {
    return new StringTokenizer(str, delim).getTokens();
  }
  
  public StringTokenizer(String str, String delim, boolean returnDelims)
  {
    super(str, delim, returnDelims);
  }


  public StringTokenizer(String str, String delim)
  {
    super(str, delim);
  }


  public StringTokenizer(String str)
  {
    super(str);
  }


  public final String[] getTokens()
  {
    String[] tokens = new String[countTokens()];
    int i = 0;
    while (hasMoreTokens())
      tokens[i++] = nextToken();
    return tokens;
  }
}
