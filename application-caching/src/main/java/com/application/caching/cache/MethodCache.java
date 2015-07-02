package com.application.caching.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an Annotation class. This annotation indicates to cache method results.
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
