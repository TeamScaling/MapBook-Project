package com.scaling.libraryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private LibraryFindService libraryFindService;

    @Autowired
    private ApiQuerySender apiQuerySender;
    @Autowired
    private MapBookService mapBookService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {

    }

    @Test
    public void evaluate_hasBook_verse_basic(){
        /* given */

        String isbn13 = "9791163032212";
        Integer areaCd = 26200;

        var dto = new ReqMapBookDto(isbn13,37.4532099, 127.1365699);

        /* when */

        var library1= libraryFindService.getNearByLibraries(dto);
        var library2 = libraryFindService.getNearByLibraries(dto.getAreaCd());

        /* then */

        var result1 = mapBookService.matchMapBooks(library1,dto);
        var result2 = mapBookService.matchMapBooks(library2,dto);

        boolean check = result1.size() <= result2.size();

        assertEquals(check,true);
    }

    @Test @DisplayName("요청 받은 위/경도로 지역 코드 추출2 [안산 지역]")
    public void update_areadCd(){
        /* given */
        String isbn13 = "9791163032212";
        ReqMapBookDto reqMapBookDto = new ReqMapBookDto(isbn13,37.247687, 126.604069);

        /* when */
        libraryFindService.outPutAreaCd(reqMapBookDto);
        /* then */

        var libraries = libraryFindService.getNearByLibraries(reqMapBookDto);

        libraries.forEach(System.out::println);
    }

    @Test @DisplayName("appConfig에 등록된 libraryCache bean load")
    public void library_cache_bean_load(){
        /* given */

        String beanNm1 = "libraryCache";
        String beanNm2 = "mapBookCache";

        /* when */

        var libBean = applicationContext.getBean(beanNm1);
        var mapBookBean = applicationContext.getBean(beanNm2);

        /* then */

        assertNotNull(libBean);
        assertNotNull(mapBookBean);

    }

    @Test
    public void find_all_class_annotation(){
        /* given */


        /* when */

        /* then */
    }

}
