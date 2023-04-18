package com.scaling.libraryservice;

import com.scaling.libraryservice.dto.TestDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

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

        List<TestDto> list = new ArrayList<>();

        list.add(new TestDto(1,"dignzh"));
        list.add(new TestDto(1,"dignzh"));

        System.out.println(new HashSet<>(list));

    }

}
