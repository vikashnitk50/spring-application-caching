package com.application.caching.cache.service.impl;

import java.util.Set;

import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.application.caching.cache.service.CacheService;

/**
 * This class holds reference to cache manager object. This can be used to manage cached objects.
 * 
 * @author vsinha
 * @version 1.0
 * @date 05-July-2015
 */
public class LRUMapCacheServiceImpl implements InitializingBean, CacheService {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(LRUMapCacheServiceImpl.class);

  /** The cache. */
  private LRUMap cache;

  /**
   * Sets the cache.
   * 
   * @param cache
   *          the new cache
   */
  public void setCache(LRUMap cache) {
    this.cache = cache;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(cache, "A cache is required. Use setCache(Cache) to provide one.");
    logger.debug("Current LRUMapCacheServiceImpl: " + this.hashCode());
  }

  @Override
  public Integer getCount() {
    return cache.size();
  }

  /**
   * TODO: Not tested This method removes cached object with specified key.
   * 
   * @param key
   *          the key
   */
  @Override
  public void remove(String key) {
    cache.remove(key);
  }

  /**
   * TODO: Not tested This method removes all cached objects.
   */
  @Override
  public void removeAll() {
    cache.clear();
    ;
  }

  /**
   * TODO: Not tested This method remove cached objects with keys starting with specified keyStartsWith
   * parameter.
   * 
   * @param keyStartsWith
   *          the key starts with
   */

  @Override
  public Object getCacheValue(String cacheKey) {
    // Get method results from cache
    return cache.get(cacheKey);
  }

  @Override
  public void addToCache(String cacheKey, Object result) {
    // Add method results to the cache
    cache.put(cacheKey, result);
  }

  @Override
  public void removeKeysStartsWith(String targetName) {
    // Clears every thing from cache whose keys starts with targetName
    long startTime = System.currentTimeMillis();
    int cleardCount = 0;
    @SuppressWarnings("unchecked")
    Set<String> keys = cache.keySet();
    if (keys != null) {
      for (String key : keys) {
        if (key.startsWith(targetName)) {
          cache.remove(key);
          cleardCount++;
        }
      }
    }
    logger.info("[LRUMapCacheServiceImpl.MethodCache] Cleard cache for targetName: " + targetName + ", cleardCount: "
        + cleardCount + ", took: " + (System.currentTimeMillis() - startTime) + "ms");

  }

}
