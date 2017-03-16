package com.buddybank;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.buddybank.utils.StringTokenizer;

/**
 * Base class to check conditions for validity. It is built to be easily interchangeable with <code>StdAssert</code>.
 *
 * @author Ivan Saorin (US01889)
 * @since 1.0
 */

public class Check
{
  public static boolean isNull(Object obj)
  {
    return (obj == null);
  }


  public static boolean notNull(Object obj)
  {
    return (obj != null);
  }


  public static boolean notEmpty(Map<?, ?> map)
  {
    return !map.isEmpty();
  }


  public static boolean isEmpty(Map<?, ?> map)
  {
    return map.isEmpty();
  }


  public static boolean isEmpty(BigDecimal value)
  {
    return value.doubleValue() <= 0;
  }


  public static boolean isEmpty(Object[] a)
  {
    return (a.length == 0);
  }


  public static boolean notEmpty(Collection<?> coll)
  {
    boolean retval;
    retval = !coll.isEmpty();
    return retval;
  }


  public static boolean isEmpty(Collection<?> coll)
  {
    boolean retval;
    retval = coll.isEmpty();
    return retval;
  }


  public static boolean notEmpty(String string)
  {
    return !"".equalsIgnoreCase(string.trim());
  }


  public static boolean isEmpty(String string)
  {
    if (isNull(string))
      return true;
    return "".equalsIgnoreCase(string.trim());
  }


  public static boolean isValid(String string)
  {
    return (notNull(string) && notEmpty(string));
  }


  public static boolean notValid(String string)
  {
    return (isNull(string) || isEmpty(string));
  }


  public static boolean isValid(Collection<?> coll)
  {
    return (notNull(coll) && notEmpty(coll));
  }


  public static boolean notValid(Collection<?> coll)
  {
    return (isNull(coll) || isEmpty(coll));
  }


  public static boolean notValid(Map<?, ?> map)
  {
    return (isNull(map) || isEmpty(map));
  }


  public static boolean isValid(Map<?, ?> map)
  {
    return (notNull(map) && notEmpty(map));
  }


  /**
   * Function that compare two Object. Make controls for null Object, and for equals values. Source1 is the main
   * Object (where the .equals method are called)
   * @param <b>Object source1</b> The source source1
   * @param <b>Object source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two String are equals (or both null), <code>FALSE</code> otherwise
   * P.S. null ---> "" for the compare EMPTY string EQUALS to BLANK string
   */
  public static boolean compare(Object source1, Object source2)
  {
    if (source1 instanceof Long && source2 instanceof Long)
    {
      return Check.compare((Long) source1, (Long) source2);
    }
    if (source1 instanceof String && source2 instanceof String)
    {
      return Check.compare((String) source1, (String) source2);
    }
    if (source1 instanceof BigDecimal && source2 instanceof BigDecimal)
    {
      return Check.compare((BigDecimal) source1, (BigDecimal) source2);
    }
    if (Check.isNull(source1) && Check.isNull(source2))
      return true;
    if (!Check.isNull(source1) && Check.isNull(source2))
      return false;
    if (Check.isNull(source1) && !Check.isNull(source2))
      return false;
    return source1.equals(source2);
  }


  /**
   * Function that compare two String. Make controls for null String, and for equals values. Source1 is the main
   * String (where the .equals method are called)
   * @param <b>String source1</b> The source source1
   * @param <b>String source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two String are equals (or both null), <code>FALSE</code> otherwise
   * P.S. null ---> "" for the compare EMPTY string EQUALS to BLANK string
   */
  public static boolean compare(String source1, String source2)
  {
    String s1 = (source1 == null) ? "" : source1.trim();
    String s2 = (source2 == null) ? "" : source2.trim();
    return compare(s1, s2, false);
  }


  public static boolean contains(String source, String what)
  {
    String s1 = (source == null) ? "" : source.trim();
    return s1.contains(what);
  }


  /**
   * Function that compare two String. Make controls for null String, and for equals values. Source1 is the main
   * String (where the .equals method are called). The ignoreCase flag switch between the use of .equals and .equalsIgnoreCase
   * @param <b>String source1</b> The source source1
   * @param <b>String source2</b> The source source2
   * @param <b>String ignoreCase</b> Activate "ignore case" compare type 
   * @return <b>boolean</b> <code>TRUE</code> if the two String are equals (or both null), <code>FALSE</code> otherwise
   * P.S. null ---> "" for the compare EMPTY string EQUALS to BLANK string
   */
  public static boolean compare(String source1, String source2, boolean ignoreCase)
  {
    String s1 = (source1 == null) ? "" : source1.trim();
    String s2 = (source2 == null) ? "" : source2.trim();
    if (ignoreCase)
      return StringUtils.equalsIgnoreCase(s1, s2);
    else
      return StringUtils.equals(s1, s2);
  }


  public static boolean compare(String source1, String source2, boolean ignoreCase, boolean noTrim)
  {
    String s1 = (source1 == null) ? "" : source1;
    String s2 = (source2 == null) ? "" : source2;
    if (ignoreCase)
      return StringUtils.equalsIgnoreCase(s1, s2);
    else
      return StringUtils.equals(s1, s2);
  }


  /**
   * Function that compare two Timestamp. Make controls for null String, and for equals values. Source1 is the main
   * Timestamp (where the .equals method are called)
   * @param <b>Timestamp source1</b> The source source1
   * @param <b>Timestamp source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two Timestamp are equals (or both null), <code>FALSE</code>
   * otherwise
   */
  public static boolean compare(Timestamp source1, Timestamp source2)
  {
    if (Check.isNull(source1) && Check.isNull(source2))
      return true;
    if (!Check.isNull(source1))
    {
      if (source1.equals(source2))
        return true;
    }
    return false;
  }


  

 


  /**
   * Function that compare two BigDecimal. Make controls for null BigDecimal, and for equals values. Source1 is the main
   * BigDecimal (where the .equals method are called)
   * @param <b>BigDecimal source1</b> The source source1
   * @param <b>BigDecimal source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two BigDecimal are equals (or both null), <code>FALSE</code>
   * otherwise
   */
  public static boolean compare(BigDecimal source1, BigDecimal source2)
  {
    if (Check.isNull(source1) && Check.isNull(source2))
      return true;
    if (!Check.isNull(source1) && !Check.isNull(source2))
    {
      double amnt1 = source1.doubleValue();
      double amnt2 = source2.doubleValue();
      return amnt1 == amnt2;
    }
    return false;
  }


  /**
   * Function that compare two Long. Make controls for null Long, and for equals values. Source1 is the main
   * Long (where the .equals method are called)
   * @param <b>Long source1</b> The source source1
   * @param <b>Long source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two Long are equals (or both null), <code>FALSE</code>
   * otherwise
   */
  public static boolean compare(Long source1, Long source2)
  {
    if (Check.isNull(source1) && Check.isNull(source2))
      return true;
    if (!Check.isNull(source1) && !Check.isNull(source2))
    {
      long amnt1 = source1.longValue();
      long amnt2 = source2.longValue();
      return amnt1 == amnt2;
    }
    return false;
  }


  /**
   * Function that compare two byte[]. Make controls for null String, and for equals values. Source1 is the main
   * byte[] (where the .equals method are called)
   * @param <b>byte[] source1</b> The source source1
   * @param <b>byte[] source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two byte[] are equals (or both null), <code>FALSE</code> otherwise
   */
  public static boolean compare(byte[] source1, byte[] source2)
  {
    if (Check.isNull(source1) && Check.isNull(source2))
      return true;
    if (!Check.isNull(source1))
    {
      if (source1.equals(source2))
        return true;
    }
    return false;
  }


  /**
   * Function that compare two Integer. Make controls for null Integer, and for equals values. Source1 is the main
   * Integer (where the .equals method are called)
   * @param <b>Integer source1</b> The source source1
   * @param <b>Integer source2</b> The source source2
   * @return <b>boolean</b> <code>TRUE</code> if the two Integer are equals (or both null), <code>FALSE</code> otherwise
   */
  public static boolean compare(Integer source1, Integer source2)
  {
    if (Check.isNull(source1) && Check.isNull(source2))
      return true;
    if (!Check.isNull(source1))
    {
      if (source1.equals(source2))
        return true;
    }
    return false;
  }


  public static boolean isNullOrEmpty(String source)
  {
    if (isNull(source) || isEmpty(source))
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(HashMap<String, String> map)
  {
    if (isNull(map) || isEmpty(map))
      return true;
    return false;
  }
  
  public static boolean isNullOrEmpty(Map<?, ?> map)
  {
    if (isNull(map) || isEmpty(map))
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(StringBuilder source)
  {
    if (isNull(source) || isEmpty(source.toString()))
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(Collection<?> source)
  {
    if (isNull(source) || isEmpty(source))
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(List<?> source)
  {
    if (isNull(source) || isEmpty(source))
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(Set<?> source)
  {
    if (isNull(source) || source.size() == 0)
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(Object[] source)
  {
    if (isNull(source) || isEmpty(source))
      return true;
    return false;
  }


  public static boolean isNullOrEmpty(BigDecimal source)
  {
    if (isNull(source) || source.doubleValue() <= 0)
      return true;
    return false;
  }


  public static boolean size(Collection<?> coll, int desired)
  {
    return (coll.size() == desired);
  }


  public static boolean size(List<?> list, int desired)
  {
    return (list.size() == desired);
  }


  public static boolean size(Object[] coll, int desired)
  {
    return (coll.length == desired);
  }


  public static boolean sizeGreaterThan(Collection<?> coll, int desired)
  {
    return (desired < coll.size());
  }

  public static boolean sizeGreaterThan(Object[] coll, int desired)
  {
    return (desired < coll.length);
  }
  
  public static boolean sizeLesserThan(Collection<?> coll, int desired)
  {
    return (coll.size() < desired);
  }


  public static boolean hasOneElement(Collection<?> coll)
  {
    return size(coll, 1);
  }


  /**
   * Return true if the length of the source String is less or equals to the source desired length
   * @param str
   * @param desired
   */
  public static boolean length(String str, int desired)
  {
    return (str.length() <= desired);
  }


  public static <T> boolean contains(T source, List<T> list)
  {
    if (source != null && !source.toString().equals("") && list != null && list.size() > 0)
      return list.contains(source);
    return false;
  }


  /**
   * @param user
   * @param clazz
   */
  public static boolean isInstance(Object obj, Class<?> clazz)
  {
    return clazz.isInstance(obj);
  }


  public static boolean isValid(int[] i)
  {
    return Check.notNull(i) && (i.length > 0);
  }


  public static boolean notIn(Object c, Object[] a)
  {
    return !Check.in(c, a);
  }


  public static boolean in(Object c, Object[] a)
  {
    boolean result = false;
    for (Object o : a)
    {
      result = result || o.equals(c);
      if (result)
        break;
    }
    return result;
  }


  public static boolean isValid(Object[] a)
  {
    return (Check.notNull(a) && (a.length > 0));
  }


  public static String retireveFormat(Date data)
  {
    String format = "yyyy-MM-dd";
    try
    {
      if (notNull(data))
      {
        String dataString = StringUtils.trim(data.toString());
        int result = dataString.indexOf("/");
        if (result < 0)
        {
          result = dataString.indexOf("-");
          if (result < 0)
            throw new ParseException("Il Formato della data e' errato", result);
          else
          {
            if (result == 2)
              format = "dd-MM-yyyy";
            else if (result == 4)
              format = "yyyy-MM-dd";
          }
        }
        else
        {
          if (result == 2)
            format = "dd/MM/yyyy";
          else if (result == 4)
            format = "yyyy/MM/dd";
        }
      }
    }
    catch (ParseException e)
    {
      throw new SystemException(e.getCause());
    }
    return format;
  }


  public static String retrieveFormat(String dataString)
  {
    String format = null;
    int result = dataString.indexOf("/");
    if (result < 0)
    {
      result = dataString.indexOf("-");
      if (result < 0)
        format = "yyyy-MM-dd";
      else
      {
        if (result == 2)
          format = "dd-MM-yyyy";
        else if (result == 4)
          format = "yyyy-MM-dd";
      }
    }
    else
    {
      if (result == 2)
        format = "dd/MM/yyyy";
      else if (result == 4)
        format = "yyyy/MM/dd";
    }
    return format;
  }


  

  public static boolean isURL(String url)
  {
    if (url == null)
    {
      return false;
    }
    //Assigning the url format regular expression
    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})(:([0-9])+)?[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
    if (url.matches(urlPattern))
    {
      return true;
    }
    else
    {
      return false;
    }

  }
  
  public static boolean compareIgnoreLeadingZeros(String source1, String source2)  {
    String s1 = (source1 == null) ? "" : source1.trim();
    String s2 = (source2 == null) ? "" : source2.trim();
    String s01, s02;
    int i=0;
    int nrOfLeadingZeros=0;
    while (s1.charAt(i)=='0')
      i++;
    nrOfLeadingZeros=i;
    s01 =  s1.substring(nrOfLeadingZeros);
    i=0;
    nrOfLeadingZeros=0;
    while (s2.charAt(i)=='0')
      i++;
    nrOfLeadingZeros=i;
    s02 =  s2.substring(nrOfLeadingZeros);  
      
    return compare(s01, s02, false);
  }
  
  public static boolean startsWith(String string, String template) {
    String[] templates = StringTokenizer.tokenize(template, ", ", false);
    int start = 0;
    boolean res = true;
    try {
      for (String tmp : templates) {
        String type = tmp.substring(tmp.length() - 1);
        int len = Integer.parseInt(tmp.substring(0, tmp.length()-1));
        int l = Math.min(len, string.length());
        int ll = start + l;
        for (int i = start; i < ll; i++) {
          char c = string.charAt(i);
          if ("a".equals(type))
            res = res && Character.isLetter(c);
          else if ("n".equals(type))
            res = res && Character.isDigit(c);
          else if ("c".equals(type))
            res = res && (Character.isLetter(c) || Character.isDigit(c));

          if (!res)
            return false;

        }
        start += l;
      }
    } catch (Throwable t) {
      return false;
    }
    return true;
  }
   
}
