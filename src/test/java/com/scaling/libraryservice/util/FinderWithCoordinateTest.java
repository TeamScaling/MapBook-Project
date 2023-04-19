package com.scaling.libraryservice.util;

import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.dto.Location;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinderWithCoordinateTest {


    @Autowired
    private LibraryRepository libraryRepo;

    @Test
    public void test(){
        /* given */

        List<Library> libraries = libraryRepo.findAll();

        Location location = new Location(35.8094167,127.147738);


        /* when */


        /*var result = finder.findAroundLibraries(location);

        *//* then *//*
        result.forEach(System.out::println);
        assertEquals(10,result.size());*/

    }

}