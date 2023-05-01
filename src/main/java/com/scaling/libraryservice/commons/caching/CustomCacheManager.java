package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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


    public void registerCaching(Cache<CacheKey,T> cache,Class<?> customer){
        log.info("[{}] is registered in caching System",customer);
        anonymousCache.put(customer,cache);

    }

    public void registerPersonalKey(Class<?> customer,Class<CacheKey> cacheKey){

        personalKeyMap.put(customer,cacheKey);

    }

    public void put(Class<?> customer, CacheKey personalKey,Object item){

        if(anonymousCache.containsKey(customer)){
            log.info("CacheManger put item for [{}]",customer);
            anonymousCache.get(customer).put(personalKey,(T)item);
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
