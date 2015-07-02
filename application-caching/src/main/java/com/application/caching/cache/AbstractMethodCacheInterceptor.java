package com.application.caching.cache;

import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class intercepts method invocation and checks for MethodCache annotation. If this annotation is
 * specified for this method, then it will cache the results.
 * 
 * @author vsinha
 * @version 1.0
 * @date 03-July-2015
 * 
 */
public abstract class AbstractMethodCacheInterceptor implements MethodInterceptor {

  /** The logger. */

  private static final Logger logger = LoggerFactory.getLogger(AbstractMethodCacheInterceptor.class);

  /**
   * main method caches method result if method is configured for caching method results must be serializable.
   * 
   * @param invocation
   *          the invocation
   * @return the object
   * @throws Throwable
   *           the throwable
   */
  @Override
  public final Object invoke(MethodInvocation invocation) throws Throwable {
    logger.info("method " + invocation.getMethod() + " is called on " + invocation.getThis() + " with args "
        + invocation.getArguments());
    Object result = null;
    MethodCache methodCacheAnnotation = invocation.getMethod().getAnnotation(MethodCache.class);
    if (methodCacheAnnotation != null) {// Results are cacheble, so cache the method result if there is no
                                        // record found in local cache
      result = cacheMethodResultIfAbsent(invocation, methodCacheAnnotation);
    } else if (invocation.getMethod().getAnnotation(InvalidateMethodCache.class) != null) {// Evict the cache
      // String targetName = invocation.getThis().getClass().getName() + ".";
      result = evictMethodResult(invocation);
    } else {// Results are not cacheble, so proceed with normal invocation
      result = invocation.proceed();
    }
    logger.info("method " + invocation.getMethod() + " returns " + result);
    return result;
  }

  private Object cacheMethodResultIfAbsent(MethodInvocation invocation, MethodCache methodCacheAnnotation)
      throws Throwable {
    // String targetName = invocation.getThis().getClass().getName();
    String targetName = invocation.getThis().toString();
    String methodName = invocation.getMethod().getName();
    Object[] arguments = invocation.getArguments();
    // In case of Tenant,String tenantId=null;
    String cacheKey = getCacheKey(targetName, methodName, arguments);
    Object result = get(cacheKey);
    if (result == null) {
      logger.info("[MethodCache] Data not found in cache for cacheKey: " + cacheKey);
      result = invocation.proceed();
      // Add result to the Cache
      if (result != null || methodCacheAnnotation.cacheNullResult()) {
        put(cacheKey, result);
      }
    } else {
      logger.info("[MethodCache] Data found in cache for cacheKey: " + cacheKey);
    }
    return result;
  }

  private Object evictMethodResult(MethodInvocation invocation) throws Throwable {
    // String targetName = invocation.getThis().getClass().getName() + ".";
    String targetName = invocation.getThis().toString() + ".";
    remove(targetName);
    Object result = invocation.proceed();
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
  private String getCacheKey(String targetName, String methodName, Object[] arguments) {
    StringBuffer sb = new StringBuffer();
    sb.append(targetName).append(".").append(methodName);
    if ((arguments != null) && (arguments.length != 0)) {
      for (int i = 0; i < arguments.length; i++) {
        if (arguments[i] != null && (arguments[i].getClass().isArray() || arguments[i] instanceof Collection)) {
          sb.append(".").append(new HashCodeBuilder().append(arguments[i]).hashCode());
        } else {
          sb.append(".").append(arguments[i]);
        }
      }
    }
    return sb.toString();
  }

  public abstract Object get(String cacheKey);

  public abstract void put(String cacheKey, Object result);

  public abstract void remove(String targetName);

}
