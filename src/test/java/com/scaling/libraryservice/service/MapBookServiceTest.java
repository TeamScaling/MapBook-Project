package com.scaling.libraryservice.service;

import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.entity.Location;
import com.scaling.libraryservice.repository.LibraryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MapBookServiceTest {

    @Autowired
    private MapBookService mapBookService;

    @Autowired
    private LibraryRepository libraryRepo;

    @Autowired
    private LibraryOpenApiService sender;

    @Test @DisplayName("지역 도서관 대출 가능 여부 데이터 생성")
    public void exist_book_with_location(){
        /* given */

        String isbn = "9788994492032";
        String area = "성남";

        /* when */

        /*List<RespBookMapDto> result = mapSearchBookService.getMapBooks();

        *//* then *//*


        assertNotEquals(0,result.size());
        result.forEach(System.out::println);*/
    }

    @Test
    public void around_libraries_accuracy(){
        /* given */

        List<Library> libraries = libraryRepo.findAll();

        Location location = new Location(35.8094167,127.147738);


        /* when */


        /*var result = mapSearchBookService.findAroundLibraries(location);

        *//* then *//*
        result.forEach(System.out::println);
        assertEquals(10,result.size());*/

    }





}