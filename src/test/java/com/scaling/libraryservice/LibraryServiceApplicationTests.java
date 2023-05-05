package com.scaling.libraryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private LibraryFindService libraryFindService;

    @Autowired
    private ApiQuerySender apiQuerySender;

    @Autowired
    private MapBookService mapBookService;
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

        var result1 = mapBookService.matchMapBooks(library1,dto);
        var result2 = mapBookService.matchMapBooks(library2,dto);

        boolean check = result1.size() <= result2.size();

        assertEquals(check,true);
    }

}
