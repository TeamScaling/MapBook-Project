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
 * @param <K> 캐시 키의 타입
 * @param <I> 캐시 항목의 타입
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomCacheAspect<K, I> {

    private final CustomCacheManager<K, I> cacheManager;

    private final double CACHE_SEC_THRESHOLD = 2.0;

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

        // 해당 클래스로 등록된 caffeineCache 인스턴스가 있는지 찾기 위해
        Class<?> customer = joinPoint.getTarget().getClass();

        // 타겟 메소드의 매개 변수에서 해당하는 캐쉬 키를 찾는다.
        CacheKey<K, I> cacheKey = cacheManager.generateCacheKey(joinPoint.getArgs());

        // caching을 사용하고 있지 않다면, 먼저 등록을 한다. 그리고 마지막에 등록된 캐싱 객체에 캐시 데이터를 넣음
        if (!cacheManager.isUsingCaching(customer)) {

            cacheManager.registerCaching((Cache<CacheKey<K, I>, I>) cacheKey.configureCache(),
                customer);
        } else {

            if (cacheManager.isContainItem(customer, cacheKey)) {
                log.info("Cache Manager find this item ");
                return cacheManager.get(customer, cacheKey);
            }
        }

        return patchCacheManager(joinPoint, customer, cacheKey);
    }

    /**
     * 이 메서드는 캐시 관리자(CacheManager)를 수정하여 메서드의 실행 결과를 캐시에 저장합니다. 실행 시간이 2초를 초과하거나 ApiRelatedService에
     * 관련된 경우에만 결과를 캐시에 저장합니다.
     *
     * @param joinPoint 프록시된 메서드에 대한 정보를 제공하는 객체
     * @param customer  캐싱 대상 객체의 클래스 정보
     * @param cacheKey  캐시에 저장될 값의 키
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 도중 예외가 발생할 경우
     */
    public I patchCacheManager(ProceedingJoinPoint joinPoint, Class<?> customer,
        CacheKey<K, I> cacheKey)
        throws Throwable {
        StopWatch stopWatch = new StopWatch();

        // 캐싱이 필요한지를 판별하기 위해 해당 메소드의 시작과 끝나는 시간을 측정
        stopWatch.start();
        I result = (I) joinPoint.proceed();
        stopWatch.stop();

        // 메소드가 2초를 초과하거나 api 관련한 클래스면 caching 처리 한다.
        if (stopWatch.getTotalTimeSeconds() > CACHE_SEC_THRESHOLD
            | ApiRelatedService.class.isAssignableFrom(customer)) {

            log.info("This task is related ApiRelatedService then CacheManger put this item");

            cacheManager.put(customer, cacheKey, result);
        }

        return result;
    }

}