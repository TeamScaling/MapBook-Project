package com.scaling.libraryservice;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.caching.mock.UserInfo;
import com.scaling.libraryservice.mapBook.cacheKey.HasBookCacheKey;
import com.scaling.libraryservice.mapBook.controller.MapBookController;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.recommend.cacheKey.RecommendCacheKey;
import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleDivider;
import com.scaling.libraryservice.search.util.TitleTokenizer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class LearningTest {


    private TitleTokenizer titleTokenizer = new TitleTokenizer(new Komoran(DEFAULT_MODEL.FULL));

    @Test
    public void reflection(){
        /* given */


        /* when */
        var result = HasBookCacheKey.class.getDeclaredFields();
        /* then */

        System.out.println(result[0].getName());
    }

    @Test
    public void test() {

        ExecutorService service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " hello");
                }
            });
        }

        service.shutdown();
    }

    @Test
    public void thread(){

        try {
            for(int i=0; i<3; i++){
                Thread.sleep(5000);
                System.out.println("hello "+ LocalDateTime.now());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } {

        }

    }

    @Test
    public void hashSet_List() {

        Map<String, String> map = new HashMap<>();

        var result = map.computeIfAbsent("a", s -> "A");

        System.out.println(result);
        System.out.println(map.get("a"));
    }

    @Test
    public void caffeine_cache() {
        /* given */

        ReqMapBookDto dto = new ReqMapBookDto("12345", 37.3, 124.4);
        ReqMapBookDto dto2 = new ReqMapBookDto("12345", 37.3, 124.4);


        /* when */

        System.out.println(dto.equals(dto2));

        /* then */
    }

    @Test
    public void isEnglish(){
        /* given */
        var result = TitleAnalyzer.isEnglish("mysql 8.0");
        /* when */
        System.out.println(result);
        /* then */
    }

    @Test
    public void english_korean() {
        String text = "e-mail에 꼭 필요한 알짜표현";

        var result = TitleDivider.divideKorEng(text);

        System.out.println(result);
    }

    @Test
    public void english_korean2() {
        String text = "HTML5, CSS3, Javascript 모바일 웹";

        char[] chars = text.toCharArray();

        StringBuilder engBuffer = new StringBuilder();
        StringBuilder korBuffer = new StringBuilder();

        List<String> eng = new ArrayList<>();
        List<String> kor = new ArrayList<>();

        boolean engFirst = true;
        boolean korFirst = true;

        for (char c : chars) {

            String pattern = "^[a-zA-Z\\s+]+$";

            if ((c+"").matches(pattern)) {
                if (!korBuffer.isEmpty()) {
                    kor.add(korBuffer.toString());
                    korBuffer.setLength(0);

                }

                if (c == ' ' & engFirst) {

                } else {
                    engBuffer.append(c);
                    engFirst = false;
                }

            } else  {

                if (!engBuffer.isEmpty()) {

                    eng.add(engBuffer.toString());
                    engBuffer.setLength(0);
                }

                if (c == ' ' & korFirst) {

                } else {
                    korBuffer.append(c);
                    korFirst = false;
                }

            }
        }

        if (!engBuffer.isEmpty()) {
            eng.add(engBuffer.toString());
        }

        if (!korBuffer.isEmpty()) {
            kor.add(korBuffer.toString());
        }

        Map<String, List<String>> result = new HashMap<>();

        result.put("eng", eng);
        result.put("kor", kor);

        System.out.println(result);

    }

    @Test
    public void tokenizer() {
        /* given */
        String text = "Easy web publishing with HTML";
        /* when */
        var result = titleTokenizer.tokenize(text);
        /* then */

        System.out.println();

        System.out.println(result);
    }

    @Test
    public void reflection2()
        throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        /* given */


        Method substitute = null;

        for (Method m : MapBookController.class.getMethods()){

            if(m.getName().contains("getHasBookMarkers")){
                substitute = m;
            }
        }

        Class<?> oClazz = BExistConn.class;

        Field field = BExistConn.class.getDeclaredField("apiStatus");
        ApiObserver apiStatus = (ApiObserver) oClazz.getConstructor().newInstance();

        System.out.println(apiStatus.getApiStatus().getApiUri());

    }

    @Test
    public void test_json_backUp_load(){
        /* given */

        String filePath = "cache_backup.ser";
        File backupFile = new File(filePath);

        Gson gson = new Gson();



        try (FileReader fileReader = new FileReader(backupFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            while (bufferedReader.ready()){
                System.out.println(bufferedReader.readLine());
            }

        } catch (IOException e) {

        }

        /* when */

        /* then */
    }

    @Test
    public void learning_serial(){
        /* given */

        UserInfo userInfo = new UserInfo("조인준",34,"1234");

        String filename = "userInfo.ser";

        /* when */

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream os = new ObjectOutputStream(bos);

            os.writeObject(userInfo);

            os.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* then */
    }

    @Test
    public void learning_deserial(){
        /* given */
        String filename = "userInfo.ser";

        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            UserInfo userInfo = (UserInfo) ois.readObject();

            System.out.println(userInfo);

            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        /* when */

        /* then */
    }

    @Test
    public void load_json_backUp(){
        /* given */
        String filename = "cache_backup2.ser";

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr);

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("MapBookApiHandler");

            List<ReqMapBookDto> reqMapBooks = new ArrayList<>();

            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("ReqMapBookDto\\(isbn=(.+),\\s+areaCd=(\\d+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    reqMapBooks.add(new ReqMapBookDto(matcher.group(1),Integer.valueOf(matcher.group(2))));
                }
            }

            Cache<ReqMapBookDto,List<RespMapBookDto>> mapBookApiHandlerItems = Caffeine.newBuilder().build();

            for(ReqMapBookDto req : reqMapBooks){
                var reqMapBook = customer.getJSONArray(req.toString());

                List<RespMapBookDto> result = new ArrayList<>();

                for(int i=0; i<reqMapBook.length(); i++){

                    result.add(new RespMapBookDto(reqMapBook.getJSONObject(i)));
                }

                mapBookApiHandlerItems.put(req,result);
            }

            mapBookApiHandlerItems.getIfPresent(new ReqMapBookDto("9791188427000",26200)).forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void load_json_backUp2(){
        /* given */
        String filename = "cache_backup2.ser";

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr);

            JSONObject respJsonObj = new JSONObject(reader.readLine());

            var customer = respJsonObj.getJSONObject("RecommendService");

            List<RecommendCacheKey> cacheKeys = new ArrayList<>();

            for (String s : customer.keySet()) {
                Pattern pattern = Pattern.compile("RecCacheKey\\(query=(.+)\\)");

                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    cacheKeys.add(new RecommendCacheKey(matcher.group(1)));
                }
            }

            Cache<RecommendCacheKey,List<String>> recommendItems = Caffeine.newBuilder().build();

            for(RecommendCacheKey rec : cacheKeys){
                var recStrList = customer.getJSONArray(rec.toString());

                List<String> result = new ArrayList<>();

                for(int i=0; i<recStrList.length(); i++){

                    result.add(recStrList.getString(i));
                }

                recommendItems.put(rec,result);
            }

            System.out.println(recommendItems.getIfPresent(new RecommendCacheKey("스프링")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void load_json_backUp3(){
        /* given */
        String filename = "cache_backup2.ser";

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

            Cache<BookCacheKey,RespBooksDto> cachedBooks = Caffeine.newBuilder().build();

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

            System.out.println(cachedBooks.getIfPresent(new BookCacheKey("스프링",1)));

            /*System.out.println(recommendItems.getIfPresent(new RecCacheKey("스프링")));*/

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void jsonBind() throws NoSuchFieldException, IllegalAccessException {
        /* given */
        var result = BookCacheKey.class.getDeclaredField("regrex");

        result.setAccessible(true);
        /* when */
        System.out.println((String) result.get(null));
        /* then */
    }



}
