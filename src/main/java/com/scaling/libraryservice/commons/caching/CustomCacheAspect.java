package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.scaling.libraryservice.mapBook.service.ApiRelatedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 사용자 정의 캐싱 어스펙트로, CustomCacheable 어노테이션이 적용된 메서드의 결과를 캐싱합니다. 캐싱된 데이터는 CustomCacheManager를 통해
 * 관리됩니다.
 *
 *   @param <K> 캐시 키의 타입
 *   @param <I> 캐시 항목의 타입
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomCacheAspect<K, I> {

    private final CustomCacheManager<K, I> cacheManager;

    @Pointcut("@annotation(com.scaling.libraryservice.commons.caching.CustomCacheable)")
    private void customCacheablePointcut() {
    }


    /**
     * "CustomCacheable" 어노테이션을 사용하는 메서드를 위한 어드바이스입니다. 캐시에 해당 데이터가 존재하면 캐시로부터 그 값을 반환하고, 그렇지 않은 경우
     * 메서드를 실행하고 그 결과를 캐시에 저장합니다.
     *
     * @param joinPoint 프록시된 메서드에 대한 정보를 제공하는 객체
     * @return 캐시로부터 가져온 결과 또는 실제 메서드 실행의 결과
     * @throws Throwable 메서드 실행 도중 예외가 발생할 경우
     */
    @Around("customCacheablePointcut()")
    @SuppressWarnings("unchecked")
    public I cacheAround(@NonNull ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> customer = joinPoint.getTarget().getClass();
        CacheKey<K, I> cacheKey = cacheManager.generateCacheKey(joinPoint.getArgs());

        if (!cacheManager.isUsingCaching(customer)) {

            cacheManager.registerCaching((Cache<CacheKey<K, I>, I>) cacheKey.configureCache(),
                customer);
        } else {
            if (cacheManager.isContainItem(customer, cacheKey)) {
                log.info("Cache Manager find this item ");
                return cacheManager.get(customer, cacheKey);
            }
        }

        return patchCacheManager(joinPoint,customer,cacheKey);
    }

    /**
     * 이 메서드는 캐시 관리자(CacheManager)를 수정하여 메서드의 실행 결과를 캐시에 저장합니다.
     * 실행 시간이 2초를 초과하거나 ApiRelatedService에 관련된 경우에만 결과를 캐시에 저장합니다.
     *
     * @param joinPoint 프록시된 메서드에 대한 정보를 제공하는 객체
     * @param customer 캐싱 대상 객체의 클래스 정보
     * @param cacheKey 캐시에 저장될 값의 키
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 도중 예외가 발생할 경우
     */
    public I patchCacheManager(ProceedingJoinPoint joinPoint, Class<?> customer, CacheKey<K, I> cacheKey)
        throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        I result = (I)joinPoint.proceed();
        stopWatch.stop();

        if (stopWatch.getTotalTimeSeconds() > 2.0 | ApiRelatedService.class.isAssignableFrom(customer)) {

            log.info(
                "This task is related ApiRelatedService then CacheManger put this item");

            cacheManager.put(customer, cacheKey, result);
        }

        return result;
    }

}