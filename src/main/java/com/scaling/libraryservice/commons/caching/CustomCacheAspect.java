package com.scaling.libraryservice.commons.caching;

import com.scaling.libraryservice.mapBook.cacheKey.HasBookCacheKey;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.MapBookService;
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

/**
 * 사용자 정의 캐싱 어스펙트로, CustomCacheable 어노테이션이 적용된 메서드의 결과를 캐싱합니다.
 * 캐싱된 데이터는 CustomCacheManager를 통해 관리됩니다.
 */

@Aspect
@Component @Slf4j
@RequiredArgsConstructor
public class CustomCacheAspect<T> {
    private final CustomCacheManager<T> cacheManager;

    @Pointcut("@annotation(com.scaling.libraryservice.commons.caching.CustomCacheable)")
    public void customCacheablePointcut() {}

    /**
     * CustomCacheable 어노테이션이 적용된 메서드에 Around 어드바이스를 적용합니다.
     * 캐싱된 데이터가 존재하면 캐시에서 결과를 반환하고, 그렇지 않으면 메서드를 실행하고 결과를 캐싱합니다.
     *
     * @param joinPoint 프록시된 메서드에 대한 정보를 포함하는 JoinPoint 객체
     * @return 캐싱된 메서드 결과 또는 새로 실행된 메서드 결과
     * @throws Throwable reflect에 의한 메서드 실행 중 예외가 발생하면 던져진다.
     */
    @Around("customCacheablePointcut()")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> clazz = joinPoint.getTarget().getClass();
        Object[] arguments = joinPoint.getArgs();
        CacheKey cacheKey = generateCacheKey(clazz, arguments);

        if (cacheManager.isContainItem(clazz, cacheKey)) {
            return cacheManager.get(clazz, cacheKey);
        }

        Object result = joinPoint.proceed();
        cacheManager.put(clazz, cacheKey, (T)result);
        return result;
    }

    /**
     * 주어진 클래스와 인수를 사용하여 CacheKey 객체를 생성합니다.
     * 적절한 CacheKey 구현을 찾지 못하면 UnsupportedOperationException을 발생시킵니다.
     *
     * @param clazz    캐시 키를 생성할 클래스
     * @param arguments 캐시 키를 생성하는 데 필요한 인수
     * @return 생성된 CacheKey 객체
     * @throws UnsupportedOperationException 적절한 CacheKey 구현을 찾지 못한 경우 던져 진다.
     */
    private CacheKey generateCacheKey(Class<?> clazz, Object[] arguments) {

        if (clazz == LibraryFindService.class) {
            String isbn13 = (String) arguments[0];
            Integer areaCd = (Integer) arguments[1];
            return new HasBookCacheKey(isbn13, areaCd);
        }

        if (clazz == MapBookService.class){

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