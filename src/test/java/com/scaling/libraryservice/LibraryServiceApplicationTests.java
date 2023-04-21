package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.util.CsvMerger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private CsvMerger csvMerger;

    @Test
    void contextLoads() {

    }

    @Test
    void merge(){

        csvMerger.merge();

    }

    @Test
    void merge3(){
        csvMerger.merge3();
    }


}
