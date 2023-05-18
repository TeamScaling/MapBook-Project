package com.scaling.libraryservice.commons.caching;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class CacheBackupServiceTest<K,V> {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void load() {
//        /* given */
//        CacheBackupService<K,V> cacheBackupService = new CacheBackupService<>(applicationContext);
//        /* when */
//
//        Map<Class<?>, Class<? extends CacheKey>> personalKeyMap = new HashMap<>();
//
//        personalKeyMap.put(BookSearchService.class, BookCacheKey.class);
//        personalKeyMap.put(MapBookService.class, ReqMapBookDto.class);
//        personalKeyMap.put(LibraryFindService.class, HasBookCacheKey.class);
//
//        //BookCacheKey(query=스프링, page=1)
//
//        var result = cacheBackupService.reloadBookCache(
//            cacheBackupService.COMMONS_BACK_UP_FILE_NAME);
//
//        System.out.println(result);
//
//        /* then */
    }

}