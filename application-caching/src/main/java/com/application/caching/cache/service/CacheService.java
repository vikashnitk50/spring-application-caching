package com.application.caching.cache.service;


public interface CacheService {

  /**
   * Gets the number of records in the cache.
   * 
   * @return the number of records
   */
  public Integer getCount();

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

  public Object getCacheValue(String cacheKey);

  public void addToCache(String cacheKey, Object result);

  /**
   * This method remove cached objects with keys starting with specified keyStartsWith parameter.
   * 
   * @param keyStartsWith
   *          the key starts with
   */
  public void removeKeysStartsWith(String keyStartsWith);

}
