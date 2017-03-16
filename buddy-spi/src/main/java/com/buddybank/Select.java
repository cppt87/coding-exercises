package com.buddybank;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Select
{
  /** select the first element of a collection if, and
   * only if the collection contains only one element. 
   * @param <T> the type of the elements within the collection
   * @param coll the collection
   * @return the first element
   * @throws AssureFailedException if the collection 
   */
  public static <T> T unique(Collection<T> coll) {
    Assure.size(coll, 1);
    return coll.iterator().next();
  }
  
  /** select the first element of a collection. 
   * @param <T> the type of the elements within the collection
   * @param coll the collection
   * @return the first element
   * @throws AssureFailedException if the collection is empty 
   */  
  public static <T> T first(Collection<T> coll) {
    Assure.sizeGreaterThan(coll, 0);
    return coll.iterator().next();
  }
  
  public static <T> T first (T [] array) {
    Assure.sizeGreaterThan(array, 0);
    return array[0];
  }
  
  /** select the last element of a list. 
   * @param <T> the type of the elements within the collection
   * @param coll the collection
   * @return the first element
   * @throws AssureFailedException if the collection is empty 
   */    
  public static <T> T last(List<T> list) {
    Assure.sizeGreaterThan(list, 0);
    return list.get(list.size() - 1);
  }
  
  /** select the last element of a collection. 
   * @param <T> the type of the elements within the collection
   * @param coll the collection
   * @return the first element
   * @throws AssureFailedException if the collection is empty 
   */      
  public static <T> T last(Collection<T> coll) {
    Assure.sizeGreaterThan(coll, 0);
    T t = null;
    Iterator<T> i = coll.iterator();
    while (i.hasNext()) {
      t = i.next();
    }
    return t;
  } 

}
