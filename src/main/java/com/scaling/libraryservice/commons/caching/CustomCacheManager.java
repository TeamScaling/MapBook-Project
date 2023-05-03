package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.scaling.libraryservice.commons.timer.Timer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@link CustomCacheManager}는 여러 개의 캐시 인스턴스를 관리하고 캐시의 라이프사이클을 조절합니다.
 * 이 클래스는 캐시 객체를 저장하고, 캐시에 데이터를 추가하거나 가져오는 기능을 제공합니다.
 *
 * @param <T> 캐시에서 저장할 데이터 유형
 */
@Slf4j @Component
public class CustomCacheManager<T> {

    private final Map<Class<?>, Cache<CacheKey,T>> anonymousCache = new HashMap<>();

    private final Map<Class<?>,Class<CacheKey>> personalKeyMap = new HashMap<>();

    public Set<Class<?>> getCustomers(){

        return new HashSet<>(anonymousCache.keySet());
    }

    public Class<CacheKey> findPersonalKey(Class<?> customer){

       return personalKeyMap.get(customer);
    }

    public Set<Entry<Class<?>, Cache<CacheKey,T>>> getEntrySet(){

        return anonymousCache.entrySet();
    }

    /**
     * 캐시를 등록하여 캐시 관리 시스템에 추가합니다.
     *
     * @param cache 등록할 캐시 인스턴스
     * @param customer 캐시를 사용하는 클래스 정보
     */
    public void registerCaching(Cache<CacheKey,T> cache,Class<?> customer){
        log.info("[{}] is registered in caching System",customer);
        anonymousCache.put(customer,cache);

    }

    public void registerPersonalKey(Class<?> customer,Class<CacheKey> cacheKey){

        personalKeyMap.put(customer,cacheKey);

    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템을 캐시에 추가합니다.
     *
     * @param customer 아이템을 추가할 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @param item 캐시에 추가할 아이템
     */
    public void put(Class<?> customer, CacheKey personalKey,Object item){

        if(anonymousCache.containsKey(customer)){
            log.info("CacheManger put item for [{}]",customer);
            anonymousCache.get(customer).put(personalKey,(T)item);
        }
    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템을 캐시에서 가져옵니다.
     *
     * @param customer 아이템을 가져올 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @return 캐시에서 가져온 아이템
     */
    @Timer
    public T get(Class<?> customer,CacheKey personalKey){
        log.info("CacheManger find item for [{}]",customer);

        return anonymousCache.get(customer).getIfPresent(personalKey);
    }

    /**
     * 주어진 클래스의 캐시를 제거합니다.
     *
     * @param customer 제거할 캐시의 클래스 정보
     */
    public void removeCaching(Class<?> customer){

        if (anonymousCache.containsKey(customer)){
            anonymousCache.remove(customer);
        }else{
            log.error("해지 하고자 하는 캐싱 정보가 없습니다. [{}]",customer);
            throw new IllegalArgumentException();
        }
    }

    /**
     * 주어진 클래스가 캐시를 사용 중인지 확인합니다.
     *
     * @param customer 확인할 클래스 정보
     * @return 캐시를 사용 중이면 true, 그렇지 않으면 false
     */
    public boolean isUsingCaching(Class<?> customer){

        return anonymousCache.containsKey(customer);
    }

    /**
     * 주어진 클래스와 개인 키에 해당하는 아이템이 캐시에 포함되어 있는지 확인합니다.
     *
     * @param customer 확인할 클래스 정보
     * @param personalKey 아이템에 대한 개인 키
     * @return 아이템이 캐시에 포함되어 있으면 true, 그렇지 않으면 false
     */
    public boolean isContainItem(Class<?> customer,CacheKey personalKey){

        if(isUsingCaching(customer)){
            return anonymousCache.get(customer).getIfPresent(personalKey) != null;
        }else{

            throw new IllegalArgumentException(customer+"is not registered for caching");
        }
    }
}
