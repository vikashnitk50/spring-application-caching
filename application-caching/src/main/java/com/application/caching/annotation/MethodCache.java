package com.application.caching.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an Annotation class. This annotation indicates to cache method results.
 * <p>
 * This is a single annotation you make the entire method result cacheable in memory
 * </p>
 * <p>
 * This is a pluggable component, just plug with your application and get the caching functionality.
 * </p>
 * 
 * @author vsinha
 * @version 1.0
 * @date 01-July-2015
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MethodCache {

  /**
   * If true, It will cache null value returned by the method.
   * 
   * @return true, if successful
   */
  public boolean cacheNullResult() default false;

}
