package com.application.caching.interceptor;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.application.caching.annotation.InvalidateMethodCache;
import com.application.caching.annotation.MethodCache;
import com.application.caching.cache.service.CacheService;

/**
 * This class intercepts method invocation and checks for MethodCache annotation. If this annotation is
 * specified for this method, then it will cache the results. Used Template design pattern...
 * 
 * @author vsinha
 * @version 1.0
 * @date 03-July-2015
 * 
 */
public class ApplicationCacheMethodInterceptor implements MethodInterceptor, InitializingBean {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(ApplicationCacheMethodInterceptor.class);

  /** The cache. */
  private CacheService cacheService;

  /**
   * sets cache name to be used.
   * 
   * @param cache
   *          the new cache
   */
  public void setCacheService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  /**
   * Checks if required attributes are provided.
   * 
   * @throws Exception
   *           the exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    debugLog("application cache is configured with " + cacheService.getClass());
    Assert.notNull(cacheService, "A cache is required. Use setCacheService(CacheService) to provide one.");
    logger.debug("[EhCacheMethodInterceptor.MethodCache] Current EhCacheMethodInterceptor: " + this.hashCode());
  }

  /**
   * main method caches method result if method is configured for caching method results must be serializable.
   * Template Method
   * 
   * @param invocation
   *          the invocation
   * @return the object
   * @throws Throwable
   *           the throwable
   */
  @Override
  public final Object invoke(MethodInvocation invocation) throws Throwable {
    debugLog("method " + invocation.getMethod() + " is called on " + invocation.getThis() + " with args "
        + invocation.getArguments());
    Object result = null;
    // If this annotation 'MethodCache' is available then results are cacheble, so cache the method result
    if (hasAnnotation(invocation, MethodCache.class)) {
      debugLog("[MethodCache]results are cacheble, so cache the method result");
      result = addToCacheIfAbsent(invocation, getAnnotation(invocation, MethodCache.class));
    } else if (hasAnnotation(invocation, InvalidateMethodCache.class)) {
      // If this annotation'InvalidateMethodCache' is availabe then clears every thing from cache whose keys
      // starts with targetName
      String targetName = invocation.getThis().toString() + ".";
      debugLog("[InvalidMethodCache] Data whose key starts with targetName " + targetName);
      cacheService.removeKeysStartsWith(targetName);
      result = invocation.proceed();
    } else {// Results are not cacheble, so proceed with normal invocation
      debugLog("Results are not cacheble, so proceed with normal invocation");
      result = invocation.proceed();
    }
    debugLog("method " + invocation.getMethod() + " returns " + result);
    return result;
  }

  private Object addToCacheIfAbsent(MethodInvocation invocation, MethodCache methodCacheAnnotation) throws Throwable {
    String proxyTargetName = invocation.getThis().getClass().getName();
    String targetName = invocation.getThis().toString();
    String methodName = invocation.getMethod().getName();
    Object[] arguments = invocation.getArguments();
    // In case of Tenant,String tenantId=null;
    String cacheKey = getCacheKey(proxyTargetName, targetName, methodName, arguments);
    Object result = cacheService.getCacheValue(cacheKey);
    if (result == null) {
      debugLog("[MethodCache] Data not found in cache for cacheKey: " + cacheKey);
      result = invocation.proceed();
      // Add result to the Cache
      if (result != null || methodCacheAnnotation.cacheNullResult()) {
        cacheService.addToCache(cacheKey, result);
      }
    } else {
      debugLog("[MethodCache] Data found in cache for cacheKey: " + cacheKey);
    }
    return result;
  }

  /**
   * creates cache key: targetName.methodName.argument0.argument1...
   * 
   * @param targetName
   *          the target name
   * @param methodName
   *          the method name
   * @param arguments
   *          the arguments
   * @return the cache key
   */
  private String getCacheKey(String proxyTargetName, String targetName, String methodName, Object[] arguments) {
    StringBuffer sb = new StringBuffer();
    sb.append(targetName).append(".").append(proxyTargetName).append(".").append(methodName);
    if ((arguments != null) && (arguments.length != 0)) {
      for (int i = 0; i < arguments.length; i++) {
        if (arguments[i] != null && (arguments[i].getClass().isArray() || arguments[i] instanceof Collection)) {
          sb.append(".").append(new HashCodeBuilder().append(arguments[i]).hashCode());
        } else {
          sb.append(".").append(arguments[i]);
        }
      }
    }
    debugLog("[MethodCache] get the cache key using reflection for targetName:[" + targetName + "] cacheKey:"
        + sb.toString());
    return sb.toString();
  }

  private <T extends Annotation> boolean hasAnnotation(MethodInvocation invocation, Class<T> annotationClass) {
    return this.getAnnotation(invocation, annotationClass) != null;
  }

  private <T extends Annotation> T getAnnotation(MethodInvocation invocation, Class<T> annotationClass) {
    return invocation.getMethod().getAnnotation(annotationClass);
  }

  private void debugLog(String message) {
    if (logger.isInfoEnabled()) {
      logger.info(message);
    }
  }

}
