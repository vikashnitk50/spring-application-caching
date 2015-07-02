package com.application.caching.cache;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class SpringMapCacheMethodInterceptor extends AbstractMethodCacheInterceptor implements InitializingBean {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(SpringMapCacheMethodInterceptor.class);
  /** The cache. */
  private Map<String, Object> cache = null;

  /**
   * sets cache name to be used.
   * 
   * @param cache
   *          the new cache
   */
  public void setCache(Map<String, Object> cache) {
    this.cache = cache;
  }

  /**
   * Checks if required attributes are provided.
   * 
   * @throws Exception
   *           the exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(cache, "A cache is required. Use setCache(Cache) to provide one.");
    logger.debug("[MethodCache] Current SpringMapCacheMethodInterceptor: " + this.hashCode());
  }

  @Override
  public Object get(String cacheKey) {
    // get result from cache
    Object element = cache.get(cacheKey);
    return (element == null ? null : element);
  }

  @Override
  public void put(Object result, boolean cacheNullResult, String cacheKey) {
    if (result != null || cacheNullResult) {
      // Add method result to cache
      cache.put(cacheKey, result);
    }
  }

  @Override
  public void remove(String targetName) {
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
    logger.info("[MethodCache] Cleard cache for targetName: " + targetName + ", cleardCount: " + cleardCount
        + ", took: " + (System.currentTimeMillis() - startTime) + "ms");
  }

}
