package com.scaling.libraryservice.commons.caching;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CacheBackupService<C, K, I> {
//
//    public final String COMMONS_BACK_UP_FILE_NAME = "cache_backup_common.json";
//
//    private final Gson gson = new Gson();
//
//    private final JsonObject jsonObject = new JsonObject();
//
//    private final ApplicationContext applicationContext;
//
//    public void saveCommonCacheToFile(Map<C, Cache<CacheKey<K,I>, I>> cacheMap) {
//
//        for (Map.Entry<C, Cache<CacheKey<K,I>, I>> entry : cacheMap.entrySet()) {
//            C clazz = entry.getKey();
//            Cache<CacheKey<K,I>, ?> cache = entry.getValue();
//
//            JsonObject cacheData = new JsonObject();
//
//            cache.asMap().forEach((key, value) -> {
//                cacheData.add(key.toString(), gson.toJsonTree(value));
//            });
//
//            jsonObject.add(clazz.toString(), cacheData);
//        }
//
//        try (FileWriter fw = new FileWriter(COMMONS_BACK_UP_FILE_NAME)) {
//            fw.write(gson.toJson(jsonObject));
//            log.info("Cache data saved to file: {}", COMMONS_BACK_UP_FILE_NAME);
//        } catch (IOException e) {
//            log.error("Failed to save cache data to file: {}", COMMONS_BACK_UP_FILE_NAME, e);
//        }
//    }
//
//    public Cache<CacheKey<K,I>, List<RespMapBookDto>> reloadMapBookCache(String filename) {
//
//        Cache<CacheKey<K,I>, List<RespMapBookDto>> mapBookApiHandlerItems = applicationContext.getBean(
//            "mapBookCache", Cache.class);
//
//        try (FileReader fr = new FileReader(filename);
//            BufferedReader reader = new BufferedReader(fr)) {
//
//            JSONObject respJsonObj = new JSONObject(reader.readLine());
//
//            var customer = respJsonObj.getJSONObject("MapBookService");
//
//            List<ReqMapBookDto> cacheKeyList = new ArrayList<>();
//
//            for (String s : customer.keySet()) {
//                Pattern pattern = Pattern.compile("ReqMapBookDto\\(isbn=(.+),\\s+areaCd=(\\d+)\\)");
//
//                Matcher matcher = pattern.matcher(s);
//
//                if (matcher.find()) {
//                    cacheKeyList.add(
//                        new ReqMapBookDto(matcher.group(1), Integer.valueOf(matcher.group(2))));
//                }
//            }
//
//            for (ReqMapBookDto key : cacheKeyList) {
//                var reqMapBook = customer.getJSONArray(key.toString());
//
//                List<RespMapBookDto> cachedResult = new ArrayList<>();
//
//                for (int i = 0; i < reqMapBook.length(); i++) {
//
//                    cachedResult.add(new RespMapBookDto(reqMapBook.getJSONObject(i)));
//                }
//
//                mapBookApiHandlerItems.put((CacheKey<K, I>) key, cachedResult);
//            }
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return mapBookApiHandlerItems;
//    }
//
//    public Cache<CacheKey, List<String>> reloadRecCache(String filename) {
//
//        Cache<CacheKey, List<String>> recommendItems =
//            applicationContext.getBean("recCache", Cache.class);
//
//        try (FileReader fr = new FileReader(filename);
//            BufferedReader reader = new BufferedReader(fr);) {
//
//            JSONObject respJsonObj = new JSONObject(reader.readLine());
//
//            var customer = respJsonObj.getJSONObject("RecommendService");
//
//            List<RecommendCacheKey> cacheKeys = new ArrayList<>();
//
//            for (String s : customer.keySet()) {
//                Pattern pattern = Pattern.compile("RecCacheKey\\(query=(.+)\\)");
//
//                Matcher matcher = pattern.matcher(s);
//
//                if (matcher.find()) {
//                    cacheKeys.add(new RecommendCacheKey(matcher.group(1)));
//                }
//            }
//
//            for (RecommendCacheKey rec : cacheKeys) {
//                var recStrList = customer.getJSONArray(rec.toString());
//
//                List<String> result = new ArrayList<>();
//
//                for (int i = 0; i < recStrList.length(); i++) {
//
//                    result.add(recStrList.getString(i));
//                }
//
//                recommendItems.put(rec, result);
//            }
//
//
//        } catch (IOException | JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        return recommendItems;
//    }
//
//
//    public Cache<CacheKey, RespBooksDto> reloadBookCache(String filename) {
//
//        Cache<CacheKey, RespBooksDto> cachedBooks = applicationContext.getBean("bookCache",
//            Cache.class);
//
//        try (FileReader fr = new FileReader(filename);
//            BufferedReader reader = new BufferedReader(fr)) {
//
//            JSONObject respJsonObj = new JSONObject(reader.readLine());
//
//            var customer = respJsonObj.getJSONObject("BookSearchService");
//
//            List<BookCacheKey> bookCacheKeys = new ArrayList<>();
//
//            for (String s : customer.keySet()) {
//                Pattern pattern = Pattern.compile("BookCacheKey\\(query=(.+),\\s+page=(\\d+)\\)");
//
//                Matcher matcher = pattern.matcher(s);
//
//                if (matcher.find()) {
//                    bookCacheKeys.add(
//                        new BookCacheKey(matcher.group(1), Integer.parseInt(matcher.group(2))));
//                }
//            }
//
//            for (BookCacheKey bKey : bookCacheKeys) {
//                var recStrList = customer.getJSONObject(bKey.toString());
//
//                var documents = recStrList.getJSONArray("documents");
//                var meta = recStrList.getJSONObject("meta");
//
//                List<BookDto> bookList = new ArrayList<>();
//
//                for (int i = 0; i < documents.length(); i++) {
//
//                    BookDto bookDto = new BookDto(documents.getJSONObject(i));
//                    bookList.add(bookDto);
//                }
//
//                cachedBooks.put(bKey, new RespBooksDto(new MetaDto(meta), bookList));
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return cachedBooks;
//    }
//
//
//}