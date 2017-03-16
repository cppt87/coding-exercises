package com.buddybank;


import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to assert conditions for validity. It is built to be easily interchangeable with <code>Check</code>.
 *
 * @author Ivan Saorin (US01889)
 * @since 1.0
 */

@SuppressWarnings("unused")
public class Assure
{
  
  static private Logger log = LoggerFactory.getLogger(Assure.class);
  
  public static void isNull(Object obj)
  {
    if (obj != null)
    {
      throw new AssertionFailedException("Object not null [" + obj.toString() + "]");
    }
  }


  public static void notNull(Object obj)
  {
    if (obj == null)
    {
      throw new AssertionFailedException("Null object!");
    }
  }

  public static void isValid(Collection<?> coll)
  {
    Assure.notNull(coll);
    Assure.notEmpty(coll);    
  }

  public static void notEmpty(Collection<?> coll)
  {
    if (coll.isEmpty())
      throw new AssertionFailedException("Collection empty [" + coll.toString() + "]");
  }

  public static void notEmpty(Object[] array)
  {
    if (array.length == 0)
      throw new AssertionFailedException("Collection empty [" + array.toString() + "]");
  }
  

  public static void isEmpty(Collection<?> coll)
  {
    if (!coll.isEmpty())
      throw new AssertionFailedException("Collection not empty [" + coll.toString() + "]");
  }


  public static void notEmpty(Enumeration<?> en)
  {
    if (!en.hasMoreElements())
      throw new AssertionFailedException("Enumeration empty [" + en.toString() + "]");
  }


  public static void isEmpty(Enumeration<?> en)
  {
    if (en.hasMoreElements())
    {
      throw new AssertionFailedException("Enumeration not empty [" + en.toString() + "]");
    }
  }


  public static void notEmpty(String string)
  {
    if ("".equals(string))
    {
      throw new AssertionFailedException("String empty [" + string.toString() + "]");
    }
  }


  public static void isEmpty(String string)
  {
    if (!"".equals(string))
    {
      throw new AssertionFailedException("String not empty [" + string.toString() + "]");
    }
  }


  public static void notNull(String descr, Object object)
  {
    if (Check.isNull(object))
    {
      throw new InvalidParameterException(MessageFormat.format("The parameter {0} cannot be null.", descr));
    }
  }
  
  public static void notEmpty(String descr, Collection<?> object)
  {
    if (Check.isEmpty(object))
    {
      throw new InvalidParameterException(MessageFormat.format("The parameter {0} cannot be empty.", descr));
    }
  }

  public static void notEmpty(String descr, Object[] object)
  {
    if (Check.isEmpty(object))
    {
      throw new InvalidParameterException(MessageFormat.format("The parameter {0} cannot be empty.", descr));
    }
  }
  
  
  public static void notEmpty(String descr, String object)
  {
    if (Check.isEmpty(object))
    {
      throw new InvalidParameterException(MessageFormat.format("The parameter {0} cannot be empty.", descr));
    }
  }
  
  public static void size(Collection<?> coll, int desired)
  {
    Assure.notNull(coll);
    if (coll.size() != desired)
    {
      throw new AssertionFailedException(
          MessageFormat.format("Collection of [{0}] element/s expected", desired)
      );
    }
  }

  public static void sizeGreaterThan(Collection<?> coll, int desired)
  {
    Assure.notNull(coll);
    if (coll.size() <= desired)
    {
     throw new AssertionFailedException(
          MessageFormat.format("Collection does not contains at least [{0}] element/s", desired)
      );
    }
  }

  public static void sizeGreaterThan (Object[] array, int desired)
  {
    Assure.notNull(array);
    if (array.length <= desired)
    {
       throw new AssertionFailedException(
      MessageFormat.format("Collection does not contains at least [{0}] element/s", desired)
  );
    }
  }
  
  public static void sizeLesserThan(Collection<?> coll, int desired)
  {
    Assure.notNull(coll);
    if (coll.size() < desired)
    {
      throw new AssertionFailedException(
          MessageFormat.format("Collection exceed [{0}] element/s", desired)
      );
    }
  }
  
  public static void hasOneElement(Collection<?> coll)
  {
    size(coll, 1);
  }

  public static void length(String str, int desired)
  {
    Assure.notNull(str);
    if (str.length() > desired)
      throw new AssertionFailedException(
          MessageFormat.format("The string [{0}] is greater than [{1}] character/s", 
          str, desired)
      );
  }

  /**
   * @param user
   * @param clazz
   */
  public static void isInstance(Object obj, Class<?> clazz)
  {
    if (!clazz.isInstance(obj)) {
      throw new AssertionFailedException(
          MessageFormat.format("The object [{0}] of class [{1}] is not instance of the class [{2}]", 
          obj, obj.getClass().getName(), clazz.getName())
      );      
    }    
  }

  public static void isEqualTo(Object obj1, Object obj2)
  {
    if (!obj1.equals(obj2))
      throw new AssertionFailedException(
          MessageFormat.format("Object [{0}] is not equal to object [{1}]", 
              obj1.toString(), obj2.toString())
      );
  }
  
  public static void isIdenticalTo(Object obj1, Object obj2)
  {
    if (obj1 != obj2)
      throw new AssertionFailedException(
          MessageFormat.format("Object [{0}] is not identical to [{1}]", 
              obj1.toString(), obj2.toString())
      );
  }
  
  public static void notIn(Object c, Object[] a)
  {
	  if (Check.in(c, a))
	      throw new AssertionFailedException(
              MessageFormat.format("Object [{0}] has not a permitted value", 
                  c.toString())
          );
  }


  public static void in(Object c, Object[] a)
  {
	  if (Check.notIn(c, a))
	      throw new AssertionFailedException(
              MessageFormat.format("Object [{0}] has not a permitted value", 
                  c.toString())
          );
  }
  
}
