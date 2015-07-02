package com.application.caching.cache;

import java.io.Serializable;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * This class intercepts method invocation and checks for MethodCache annotation. If this annotation is
 * specified for this method, then it will cache the results.
 * 
 * @author vsinha
 * @version 1.0
 * @date 01-July-2015
 */

public class SpringEHCacheMethodInterceptor extends AbstractMethodCacheInterceptor implements InitializingBean {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(SpringEHCacheMethodInterceptor.class);
  /** The cache. */
  private Cache cache;

  /**
   * sets cache name to be used.
   * 
   * @param cache
   *          the new cache
   */
  public void setCache(Cache cache) {
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
    logger.debug("[MethodCache] Current SpringEHCacheMethodInterceptor: " + this.hashCode());
  }

  @Override
  public Object get(String cacheKey) {
    // Get method results from cache
    Element element = cache.get(cacheKey);
    return (element == null ? null : element.getValue());
  }

  @Override
  public void put(Object result, boolean cacheNullResult, String cacheKey) {
    // Add method results to the cache
    if (result != null || cacheNullResult) {
      Element element = new Element(cacheKey, (Serializable) result);
      cache.put(element);
    }
  }

  @Override
  public void remove(String targetName) {
    // Clears every thing from cache whose keys starts with targetName
    long startTime = System.currentTimeMillis();
    int cleardCount = 0;
    @SuppressWarnings("unchecked")
    List<String> keys = cache.getKeys();
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
