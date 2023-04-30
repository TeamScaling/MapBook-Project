package com.scaling.libraryservice.commons.caching;

import com.scaling.libraryservice.mapBook.cacheKey.HasBookCacheKey;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.recommend.cacheKey.RecCacheKey;
import com.scaling.libraryservice.search.service.BookSearchService;
import com.scaling.libraryservice.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component @Slf4j
@RequiredArgsConstructor
public class CustomCacheAspect<T> {
    private final CustomCacheManager<T> cacheManager;

    @Pointcut("@annotation(com.scaling.libraryservice.commons.caching.CustomCacheable)")
    public void customCacheablePointcut() {}

    @Around("customCacheablePointcut()")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> clazz = joinPoint.getTarget().getClass();
        Object[] arguments = joinPoint.getArgs();
        CacheKey cacheKey = generateCacheKey(clazz, arguments);

        if (cacheManager.isContainItem(clazz, cacheKey)) {
            return cacheManager.get(clazz, cacheKey);
        }

        Object result = joinPoint.proceed();
        cacheManager.put(clazz, cacheKey, result);
        return result;

    }
    private CacheKey generateCacheKey(Class<?> clazz, Object[] arguments) {

        if (clazz == LibraryFindService.class) {
            String isbn13 = (String) arguments[0];
            Integer areaCd = (Integer) arguments[1];
            return new HasBookCacheKey(isbn13, areaCd);
        }

        if (clazz == MapBookApiHandler.class){

            return (ReqMapBookDto)arguments[1];
        }

        if(clazz == BookSearchService.class){

            String query = (String) arguments[0];
            int page = (int) arguments[1];

            return new BookCacheKey(query,page);
        }

        if(clazz == RecommendService.class){

            String query = (String) arguments[0];

            return new RecCacheKey(query);
        }


        throw new UnsupportedOperationException("No suitable CacheKey implementation found for class: " + clazz.getName());
    }
}