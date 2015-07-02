package com.application.caching.cache;

import java.util.List;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * This class holds reference to cache manager object. This can be used to manage cached objects.
 * 
 * @author vsinha
 * @version 1.0
 * @date 01-July-2015
 */
public class SpringEHCacheManager implements InitializingBean, CacheService {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(SpringEHCacheManager.class);

  /** The cache. */
  private Cache cache;

  /**
   * Sets the cache.
   * 
   * @param cache
   *          the new cache
   */
  public void setCache(Cache cache) {
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
    logger.debug("Current SpringCacheManager: " + this.hashCode());
  }

  @Override
  public Integer getCount() {
    return cache.getSize();
  }

  /**
   * Gets the keys.
   * 
   * @return keys of cached objects
   */
  @SuppressWarnings("rawtypes")
  @Override
  public List getKeys() {
    return cache.getKeys();
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
    cache.removeAll();
  }

  /**
   * TODO: Not tested This method remove cached objects with keys starting with specified keyStartsWith
   * parameter.
   * 
   * @param keyStartsWith
   *          the key starts with
   */
  @Override
  public void removeKeysStartsWith(String keyStartsWith) {
    @SuppressWarnings("unchecked")
    List<String> keys = cache.getKeys();
    if (keys != null) {
      for (String key : keys) {
        if (key.startsWith(keyStartsWith)) {
          cache.remove(key);
        }
      }
    }
  }

}
