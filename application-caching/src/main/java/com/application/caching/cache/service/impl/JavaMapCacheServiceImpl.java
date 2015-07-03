package com.application.caching.cache.service.impl;

import java.util.Map;
import java.util.Set;

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
public class JavaMapCacheServiceImpl implements InitializingBean, CacheService {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(JavaMapCacheServiceImpl.class);

  /** The cache. */
  private Map<String, Object> cache;

  /**
   * Sets the cache.
   * 
   * @param cache
   *          the new cache
   */
  public void setCache(Map<String, Object> cache) {
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
    logger.debug("Current JavaMapCacheServiceImpl: " + this.hashCode());
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
    Set<String> keys = cache.keySet();
    if (keys != null) {
      for (String key : keys) {
        if (key.startsWith(targetName)) {
          cache.remove(key);
          cleardCount++;
        }
      }
    }
    logger.info("[JavaMapCacheServiceImpl.MethodCache] Cleard cache for targetName: " + targetName + ", cleardCount: "
        + cleardCount + ", took: " + (System.currentTimeMillis() - startTime) + "ms");

  }

}
