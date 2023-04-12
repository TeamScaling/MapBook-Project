package com.scaling.libraryservice;

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

}
