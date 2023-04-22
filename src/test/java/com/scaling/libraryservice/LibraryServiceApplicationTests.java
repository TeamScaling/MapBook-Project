package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private LibraryFindService libraryFindService;

    @Autowired
    private ApiQuerySender apiQuerySender;

    @Test
    void contextLoads() {

    }

    @Test
    public void evaluate_hasBook_verse_basic(){
        /* given */

        String isbn13 = "9791163032212";
        Integer areaCd = 26200;

        var dto = new ReqMapBookDto(isbn13,37.4532099, 127.1365699,null,null);

        /* when */

        var library1= libraryFindService.getNearByLibrariesHasBook(dto);
        var library2 = libraryFindService.getNearByLibraries(dto);

        /* then */

        var result1 = apiQuerySender.multiQuery(library1,isbn13,10);
        var result2 = apiQuerySender.multiQuery(library2,isbn13,10);

        boolean check = result1.size() <= result2.size();

        assertEquals(check,true);
    }

}
