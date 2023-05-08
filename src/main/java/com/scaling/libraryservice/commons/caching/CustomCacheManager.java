package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@link CustomCacheManager}는 여러 개의 캐시 인스턴스를 관리하고 캐시의 라이프사이클을 조절합니다. 이 클래스는 캐시 객체를 저장하고, 캐시에 데이터를
 * 추가하거나 가져오는 기능을 제공합니다.
 *
 * @param <T> 캐시에서 저장할 데이터 유형
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomCacheManager<T> {

    private Map<Class<?>, Cache<CacheKey, T>> commonsCache = new HashMap<>();
    private final Map<Class<?>, Class<? extends CacheKey>> personalKeyMap = new HashMap<>();
    private final CacheBackupService<T> cacheBackupService;

    private final Cache<BookCacheKey, RespBooksDto> slowSearchCache = Caffeine.newBuilder().build();


    @PreDestroy
    public void onShutdown() {
        log.info("Cache size is {}",commonsCache.get(BookSearchService.class).asMap().size());
        cacheBackupService.saveCommonCacheToFile(commonsCache);
    }

    public Map<Class<?>, Cache<CacheKey, T>> getCommonsCache() {
        return commonsCache;
    }

    public Set<Class<?>> getCustomers() {

        return new HashSet<>(commonsCache.keySet());
    }

    public Class<? extends CacheKey> findPersonalKey(Class<?> customer) {

        return personalKeyMap.get(customer);
    }

    public Set<Entry<Class<?>, Cache<CacheKey, T>>> getEntrySet() {

        return commonsCache.entrySet();
    }

    /**
     * 캐시를 등록하여 캐시 관리 시스템에 추가합니다.
     *
     * @param cache    등록할 캐시 인스턴스
     * @param customer 캐시를 사용하는 클래스 정보
     */
    public void registerCaching(Cache<CacheKey, T> cache, Class<?> customer) {
        log.info("[{}] is registered in caching System", customer);
        commonsCache.put(customer, cache);

    }

    public void registerPersonalKey(Class<?> customer, Class<CacheKey> cacheKey) {

        personalKeyMap.put(customer, cacheKey);

    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템을 캐시에 추가합니다.
     *
     * @param customer    아이템을 추가할 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @param item        캐시에 추가할 아이템
     */
    public void put(Class<?> customer, CacheKey personalKey, T item) {

        if (commonsCache.containsKey(customer)) {
            log.info("CacheManger put item for [{}]", customer);
            commonsCache.get(customer).put(personalKey, (T) item);
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
    public T get(Class<?> customer, CacheKey personalKey) {
        log.info("CacheManger find item for [{}]", customer);

        return commonsCache.get(customer).getIfPresent(personalKey);
    }

    /**
     * 주어진 클래스의 캐시를 제거합니다.
     *
     * @param customer 제거할 캐시의 클래스 정보
     */
    public void removeCaching(Class<?> customer) {

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
    public boolean isUsingCaching(Class<?> customer) {

        return commonsCache.containsKey(customer);
    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템이 캐시에 포함되어 있는지 확인합니다.
     *
     * @param customer    확인할 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @return 아이템이 캐시에 포함되어 있으면 true, 그렇지 않으면 false
     */
    public boolean isContainItem(Class<?> customer, CacheKey personalKey) {

        if (isUsingCaching(customer)) {

            return commonsCache.get(customer).getIfPresent(personalKey) != null;
        } else {

            throw new IllegalArgumentException(customer + "is not registered for caching");
        }
    }
}
