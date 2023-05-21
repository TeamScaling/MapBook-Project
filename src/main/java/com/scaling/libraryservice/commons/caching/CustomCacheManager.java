package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.scaling.libraryservice.commons.timer.Timer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@link CustomCacheManager}는 여러 개의 캐시 인스턴스를 관리하고 캐시의 라이프사이클을 조절합니다. 이 클래스는 캐시 객체를 저장하고, 캐시에 데이터를
 * 추가하거나 가져오는 기능을 제공합니다.
 *
 * @param <K> 캐시에서 저장할 데이터 유형
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomCacheManager<C, K, I> {

    private final Map<C, Cache<CacheKey<K,I>, I>> commonsCache = new HashMap<>();
    private final CacheBackupService<C, K, I> cacheBackupService;

    @PreDestroy
    public void onShutdown() {
        cacheBackupService.saveCommonCacheToFile(commonsCache);
    }

    @PostConstruct
    public void reloadCacheData() {

        File file = new File(cacheBackupService.COMMONS_BACK_UP_FILE_NAME);

        if (file.exists()) {
            cacheBackupService.reloadRecCache(cacheBackupService.COMMONS_BACK_UP_FILE_NAME);
            cacheBackupService.reloadBookCache(cacheBackupService.COMMONS_BACK_UP_FILE_NAME);
            cacheBackupService.reloadMapBookCache(cacheBackupService.COMMONS_BACK_UP_FILE_NAME);
        }
    }


    /**
     * 캐시를 등록하여 캐시 관리 시스템에 추가합니다.
     *
     * @param cache    등록할 캐시 인스턴스
     * @param customer 캐시를 사용하는 클래스 정보
     */
    public void registerCaching(Cache<CacheKey<K,I>, I> cache, C customer) {
        log.info("[{}] is registered in caching System", customer);
        commonsCache.put(customer, cache);

    }


    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템을 캐시에 추가합니다.
     *
     * @param customer    아이템을 추가할 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @param item        캐시에 추가할 아이템
     */
    public void put(C customer, CacheKey<K,I> personalKey, I item) {

        if (commonsCache.containsKey(customer)) {
            log.info("CacheManger put item for [{}]", customer);
            commonsCache.get(customer).put(personalKey, (I) item);
        }
    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템을 캐시에서 가져옵니다.
     *
     * @param customer    아이템을 가져올 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @return 캐시에서 가져온 아이템
     */
    @Timer
    public I get(C customer, CacheKey<K,I> personalKey) {
        log.info("CacheManger find item for [{}]", customer);

        return commonsCache.get(customer).getIfPresent(personalKey);
    }

    /**
     * 주어진 클래스의 캐시를 제거합니다.
     *
     * @param customer 제거할 캐시의 클래스 정보
     */
    public void removeCaching(C customer) {

        if (commonsCache.containsKey(customer)) {
            commonsCache.remove(customer);
        } else {
            log.error("해지 하고자 하는 캐싱 정보가 없습니다. [{}]", customer);
            throw new IllegalArgumentException();
        }
    }

    /**
     * 주어진 클래스가 캐시를 사용 중인지 확인합니다.
     *
     * @param customer 확인할 클래스 정보
     * @return 캐시를 사용 중이면 true, 그렇지 않으면 false
     */
    public boolean isUsingCaching(C customer) {

        return commonsCache.containsKey(customer);
    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템이 캐시에 포함되어 있는지 확인합니다.
     *
     * @param customer    확인할 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @return 아이템이 캐시에 포함되어 있으면 true, 그렇지 않으면 false
     */
    public boolean isContainItem(C customer, CacheKey<K,I> personalKey) {

        if (isUsingCaching(customer)) {

            return commonsCache.get(customer).getIfPresent(personalKey) != null;
        } else {

            throw new IllegalArgumentException(customer + "is not registered for caching");
        }
    }

    /**
     * 주어진 클래스와 인수를 사용하여 CacheKey 객체를 생성합니다. 적절한 CacheKey 구현을 찾지 못하면 UnsupportedOperationException을
     * 발생시킵니다.
     *
     * @param arguments 캐시 키를 생성하는 데 필요한 인수
     * @return 생성된 CacheKey 객체
     * @throws UnsupportedOperationException 적절한 CacheKey 구현을 찾지 못한 경우 던져 진다.
     */
     CacheKey<K,I> generateCacheKey(Object[] arguments) throws UnsupportedOperationException {

        for(Object obj : arguments){

            if(obj instanceof CacheKey<?,?>){
                return (CacheKey<K,I>) obj;
            }
        }

        throw new UnsupportedOperationException(
            "No suitable CacheKey implementation found for class: ");
    }
}
