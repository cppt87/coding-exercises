package com.buddybank;

import java.text.MessageFormat;


public class ValidationException extends Exception {
  private static final long serialVersionUID = -8180910064666091176L;

  private String name;
  private String value;
  private String expected;

  public ValidationException(String pattern, Throwable cause, Object... arguments) {
    super(MessageFormat.format(pattern, arguments), cause);
    if (Check.notNull(arguments)) {
      if (Check.notNull(arguments[0])) 
        name = arguments[0].toString();
      if (Check.notNull(arguments[1]))
        value = arguments[1].toString();
      if (Check.notNull(arguments[2]))
        expected = arguments[2].toString();
    }
  }

  public ValidationException(String pattern, Object... arguments) {
    super(MessageFormat.format(pattern, arguments));
    if (Check.notNull(arguments)) {
      if (Check.notNull(arguments[0])) 
        name = arguments[0].toString();
      if (Check.notNull(arguments[1]))
        value = arguments[1].toString();
      if (Check.notNull(arguments[2]))
        expected = arguments[2].toString();
    }
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public String getExpected() {
    return expected;
  }
}
