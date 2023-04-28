package com.scaling.libraryservice.caching;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomCacheManager<T> {

    private final Map<Class<?>, Cache<CacheKey,T>> anonymousCache = new HashMap<>();

    public void registerCaching(Cache<CacheKey,T> cache,Class<?> customer){
        log.info("[{}] is registered in caching System",customer);
        anonymousCache.put(customer,cache);
    }

    public void put(Class<?> customer, CacheKey personalKey,T item){

        if(anonymousCache.containsKey(customer)){
            log.info("CacheManger put item for [{}]",customer);
            anonymousCache.get(customer).put(personalKey,item);
        }
    }

    public T get(Class<?> customer,CacheKey personalKey){
        log.info("CacheManger find item for [{}]",customer);

        return anonymousCache.get(customer).getIfPresent(personalKey);
    }

    public void removeCaching(Class<?> customer){

        if (anonymousCache.containsKey(customer)){
            anonymousCache.remove(customer);
        }else{
            log.error("해지 하고자 하는 캐싱 정보가 없습니다. [{}]",customer);
            throw new IllegalArgumentException();
        }
    }

    public boolean isUsingCaching(Class<?> customer){

        return anonymousCache.containsKey(customer);
    }

    public boolean isContainItem(Class<?> customer,CacheKey personalKey){

        if(isUsingCaching(customer)){
            return anonymousCache.get(customer).getIfPresent(personalKey) != null;
        }else{

            throw new IllegalArgumentException(customer+"is not registered for caching");
        }
    }
}
