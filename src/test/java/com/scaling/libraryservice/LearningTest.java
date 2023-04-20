package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LearningTest {


    @Test
    public void test(){

        ExecutorService service = Executors.newFixedThreadPool(5);

        for(int i=0; i<10; i++){
            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" hello");
                }
            });
        }

        service.shutdown();
    }

    @Test
    public void hashSet_List(){

        Map<String,String> map = new HashMap<>();

        var result = map.computeIfAbsent("a",s -> "A");

        System.out.println(result);
        System.out.println(map.get("a"));
    }

    @Test
    public void caffeine_cache(){
        /* given */

        ReqMapBookDto dto = new ReqMapBookDto("12345",37.3,124.4,null,null);
        ReqMapBookDto dto2 = new ReqMapBookDto("12345",37.3,124.4,null,null);


        /* when */

        System.out.println(dto.equals(dto2) );

        /* then */
    }

}
