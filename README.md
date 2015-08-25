# spring-application-caching

**Caching the result of a method using spring/java for an application.**

Let me give you a little background about this project. i have created a custion annonation to cache/invalidate method result.

@MethodCache: It will cache the result of a method

@InvalidateMethodCache: It will clear the result of a method.

ApplicationCacheMethodInterceptor: This class intercepts method invocation and checks for MethodCache annotation. If this annotation is specified for this method, then it will cache the results.

