package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.mapBook.cacheKey.HasBookCacheKey;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CacheBackupServiceTest {


    @Test
    public void load() {
        /* given */
        CacheBackupService<?> cacheBackupService = new CacheBackupService<>();
        /* when */

        Map<Class<?>, Class<? extends CacheKey>> personalKeyMap = new HashMap<>();

        personalKeyMap.put(BookSearchService.class, BookCacheKey.class);
        personalKeyMap.put(MapBookService.class, ReqMapBookDto.class);
        personalKeyMap.put(LibraryFindService.class, HasBookCacheKey.class);

        //BookCacheKey(query=스프링, page=1)

        var result = cacheBackupService.reloadBookCache(
            cacheBackupService.COMMONS_BACK_UP_FILE_NAME, Caffeine.newBuilder().build());

        System.out.println(result);

        /* then */
    }

}