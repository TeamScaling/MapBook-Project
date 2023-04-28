package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
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

    @Autowired
    private MapBookApiHandler mapBookApiHandler;
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

        var library1= libraryFindService.getNearByLibraries(dto);
        var library2 = libraryFindService.getNearByAllLibraries(dto.getAreaCd());

        /* then */

        var result1 = mapBookApiHandler.matchMapBooks(library1,dto);
        var result2 = mapBookApiHandler.matchMapBooks(library2,dto);

        boolean check = result1.size() <= result2.size();

        assertEquals(check,true);
    }

}
