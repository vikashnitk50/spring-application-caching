# spring-application-caching

**Caching the result of a method using spring/java for an application.**

Let me give you a little background about this project. i have created a custion annonation to cache/invalidate method result.

**@MethodCache:** It will cache the result of a method

**@InvalidateMethodCache:** It will clear the result of a method.

**ApplicationCacheMethodInterceptor:** This class intercepts method invocation and checks for MethodCache annotation. If this annotation is specified for this method, then it will cache the results.

**CacheController:** It will allow admin user to clear the cache from Admin UI(Take a simple example Country Table: We are not going to add a new country into Table everyday but if you add a new country into Table using insert statement.)
We can integrate this controller with UI and allow admin user to clear the Cache(Load the fresh data from the DB)

**For example:**

    public class CountryService {

        @MethodCache
 
        public List<Country> getCountries();
 

        @MethodCache
 
        public Country getCountryById(int countryId);

        @InvalidateMethodCache
 
        public Country deleteCountryByID(int countryId);

      }
