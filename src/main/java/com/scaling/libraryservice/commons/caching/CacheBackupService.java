package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.recommend.cacheKey.RecCacheKey;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheBackupService<T> {

    public final String COMMONS_BACK_UP_FILE_NAME = "cache_backup_common.json";

    public void saveCommonCacheToFile(Map<Class<?>, Cache<CacheKey, T>> cacheMap) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<Class<?>, Cache<CacheKey, T>> entry : cacheMap.entrySet()) {
            Class<?> clazz = entry.getKey();
            Cache<CacheKey, ?> cache = entry.getValue();

            JsonObject cacheData = new JsonObject();

            cache.asMap().forEach((key, value) -> {
                cacheData.add(key.toString(), gson.toJsonTree(value));
            });

            jsonObject.add(clazz.getSimpleName(), cacheData);
        }

        try (FileWriter fw = new FileWriter(COMMONS_BACK_UP_FILE_NAME)) {
            fw.write(gson.toJson(jsonObject));
            log.info("Cache data saved to file: {}", COMMONS_BACK_UP_FILE_NAME);
        } catch (IOException e) {
            log.error("Failed to save cache data to file: {}", COMMONS_BACK_UP_FILE_NAME, e);
        }


    }

    public Cache<CacheKey, List<RespMapBookDto>> reloadMapBookCache(String filename,
        Cache<CacheKey, List<RespMapBookDto>> cache) {

        Cache<CacheKey, List<RespMapBookDto>> mapBookApiHandlerItems = null;

        try (FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr)) {

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("MapBookService");

            List<ReqMapBookDto> cacheKeyList = new ArrayList<>();

            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("ReqMapBookDto\\(isbn=(.+),\\s+areaCd=(\\d+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    cacheKeyList.add(
                        new ReqMapBookDto(matcher.group(1), Integer.valueOf(matcher.group(2))));
                }
            }

            mapBookApiHandlerItems = cache;

            for (ReqMapBookDto key : cacheKeyList) {
                var reqMapBook = customer.getJSONArray(key.toString());

                List<RespMapBookDto> cachedResult = new ArrayList<>();

                for (int i = 0; i < reqMapBook.length(); i++) {

                    cachedResult.add(new RespMapBookDto(reqMapBook.getJSONObject(i)));
                }

                mapBookApiHandlerItems.put(key, cachedResult);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapBookApiHandlerItems;
    }

    public Cache<CacheKey, List<String>> reloadRecCache(String filename,Cache<CacheKey, List<String>> cache) {

        Cache<CacheKey, List<String>> recommendItems = null;

        try (FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr);) {

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("RecommendService");

            List<RecCacheKey> cacheKeys = new ArrayList<>();

            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("RecCacheKey\\(query=(.+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    cacheKeys.add(new RecCacheKey(matcher.group(1)));
                }
            }

            recommendItems = cache;

            for (RecCacheKey rec : cacheKeys) {
                var recStrList = customer.getJSONArray(rec.toString());

                List<String> result = new ArrayList<>();

                for (int i = 0; i < recStrList.length(); i++) {

                    result.add(recStrList.getString(i));
                }

                recommendItems.put(rec, result);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return recommendItems;
    }


    public Cache<CacheKey, RespBooksDto> reloadBookCache(String filename,Cache<CacheKey, RespBooksDto> cache) {

        Cache<CacheKey, RespBooksDto> cachedBooks;

        try (FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr)) {

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("BookSearchService");

            List<BookCacheKey> bookCacheKeys = new ArrayList<>();

            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("BookCacheKey\\(query=(.+),\\s+page=(\\d+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    bookCacheKeys.add(
                        new BookCacheKey(matcher.group(1), Integer.parseInt(matcher.group(2))));
                }
            }

            cachedBooks = cache;

            for (BookCacheKey bKey : bookCacheKeys) {
                var recStrList = customer.getJSONObject(bKey.toString());

                var documents = recStrList.getJSONArray("documents");
                var meta = recStrList.getJSONObject("meta");

                List<BookDto> bookList = new ArrayList<>();

                for (int i = 0; i < documents.length(); i++) {

                    BookDto bookDto = new BookDto(documents.getJSONObject(i));
                    bookList.add(bookDto);
                }

                cachedBooks.put(bKey, new RespBooksDto(new MetaDto(meta), bookList));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return cachedBooks;
    }


}
