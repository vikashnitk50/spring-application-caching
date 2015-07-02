package com.application.caching.cache;

import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author vsinha
 * @version 1.0
 * @date 01-July-2015
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
    if (methodCacheAnnotation != null) {
      // String targetName = invocation.getThis().getClass().getName();
      String targetName = invocation.getThis().toString();
      String methodName = invocation.getMethod().getName();
      Object[] arguments = invocation.getArguments();
      String cacheKey = getCacheKey(targetName, methodName, arguments);
      result = get(cacheKey);
      if (result == null) {
        logger.info("[MethodCache] Data not found in cache for cacheKey: " + cacheKey);
        result = invocation.proceed();
        // Add result to the Cache
        put(result, methodCacheAnnotation.cacheNullResult(), cacheKey);
      } else {
        logger.info("[MethodCache] Data found in cache for cacheKey: " + cacheKey);
      }

    } else if (invocation.getMethod().getAnnotation(InvalidateMethodCache.class) != null) {
      // String targetName = invocation.getThis().getClass().getName() + ".";
      String targetName = invocation.getThis().toString() + ".";
      remove(targetName);
      result = invocation.proceed();
    } else {
      // Results are not cacheble, so proceed with normal invocation
      result = invocation.proceed();
    }
    logger.info("method " + invocation.getMethod() + " returns " + result);
    return result;
  }

  public abstract Object get(String cacheKey);

  public abstract void remove(String targetName);

  public abstract void put(Object result, boolean cacheNullResult, String cacheKey);

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

}
