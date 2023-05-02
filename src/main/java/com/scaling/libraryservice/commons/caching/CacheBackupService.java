package com.scaling.libraryservice.commons.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
import com.scaling.libraryservice.recommend.cacheKey.RecCacheKey;
import com.scaling.libraryservice.recommend.service.RecommendService;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheBackupService<T> {

    private final Gson gson;

    public CacheBackupService() {
        gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Class.class, new ClassTypeAdapter())
            .create();
    }

    public void saveCacheDataToFile(String filePath, Map<Class<?>, Cache<CacheKey, T>> cacheMap) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<Class<?>, Cache<CacheKey, T>> entry : cacheMap.entrySet()) {
            Class<?> clazz = entry.getKey();
            Cache<CacheKey, T> cache = entry.getValue();

            JsonObject cacheData = new JsonObject();

            cache.asMap().forEach((key, value) -> {
                cacheData.add(key.toString(), gson.toJsonTree(value));
            });

            jsonObject.add(clazz.getSimpleName(), cacheData);
        }

        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(gson.toJson(jsonObject));
            log.info("Cache data saved to file: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to save cache data to file: {}", filePath, e);
        }
    }

    public Map<Class<?>, Cache<? extends CacheKey, ?>> loadCacheDataFromFile(String filePath,
        Map<Class<?>, Class<? extends CacheKey>> personalKeyMap) {

        Map<Class<?>, Cache<? extends CacheKey, ?>> resultMap = new HashMap<>();

        resultMap.put(BookSearchService.class,convertForBook(filePath));
        resultMap.put(RecommendService.class,convertForRec(filePath));
        resultMap.put(MapBookApiHandler.class,convertForMapBook(filePath));

        return resultMap;
    }

    public Cache<ReqMapBookDto, List<RespMapBookDto>> convertForMapBook(String filename) {

        Cache<ReqMapBookDto, List<RespMapBookDto>> mapBookApiHandlerItems = null;

        try (FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr)) {

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("MapBookApiHandler");

            List<ReqMapBookDto> cacheKeyList = new ArrayList<>();

            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("ReqMapBookDto\\(isbn=(.+),\\s+areaCd=(\\d+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    cacheKeyList.add(
                        new ReqMapBookDto(matcher.group(1), Integer.valueOf(matcher.group(2))));
                }
            }

            mapBookApiHandlerItems = Caffeine.newBuilder().build();

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

    public Cache<RecCacheKey, List<String>> convertForRec(String filename) {

        Cache<RecCacheKey, List<String>> recommendItems = null;

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

            recommendItems = Caffeine.newBuilder().build();

            for (RecCacheKey rec : cacheKeys) {
                var recStrList = customer.getJSONArray(rec.toString());

                List<String> result = new ArrayList<>();

                for (int i = 0; i < recStrList.length(); i++) {

                    result.add(recStrList.getString(i));
                }

                recommendItems.put(rec, result);
            }

            System.out.println(recommendItems.getIfPresent(new RecCacheKey("스프링")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return recommendItems;
    }

    public Cache<BookCacheKey,RespBooksDto> convertForBook(String filename){

        Cache<BookCacheKey,RespBooksDto> cachedBooks;

        try(FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr)) {

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("BookSearchService");

            List<BookCacheKey> bookCacheKeys = new ArrayList<>();



            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("BookCacheKey\\(query=(.+),\\s+page=(\\d+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    bookCacheKeys.add(new BookCacheKey(matcher.group(1),Integer.parseInt(matcher.group(2))));
                }
            }

            cachedBooks = Caffeine.newBuilder().build();

            for(BookCacheKey bKey : bookCacheKeys){
                var recStrList = customer.getJSONObject(bKey.toString());

                var documents = recStrList.getJSONArray("documents");
                var meta = recStrList.getJSONObject("meta");


                List<BookDto> bookList = new ArrayList<>();

                for(int i=0; i<documents.length(); i++){

                    BookDto bookDto = new BookDto(documents.getJSONObject(i));
                    bookList.add(bookDto);
                }

                cachedBooks.put(bKey,new RespBooksDto(new MetaDto(meta),bookList));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return cachedBooks;
    }





}
