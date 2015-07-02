package com.application.caching.cache;

import java.util.List;

public interface CacheService {

  /**
   * Gets the number of records in the cache.
   * 
   * @return the number of records
   */
  public Integer getCount();

  /**
   * Gets the keys.
   * 
   * @return keys of cached objects
   */
  @SuppressWarnings("rawtypes")
  public List getKeys();

  /**
   * This method removes cached object with specified key.
   * 
   * @param key
   *          the key
   */
  public void remove(String key);

  /**
   * This method removes all cached objects.
   */
  public void removeAll();

  /**
   * This method remove cached objects with keys starting with specified keyStartsWith parameter.
   * 
   * @param keyStartsWith
   *          the key starts with
   */
  public void removeKeysStartsWith(String keyStartsWith);

}
