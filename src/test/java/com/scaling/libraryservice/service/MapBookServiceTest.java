package com.scaling.libraryservice.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.exception.OpenApiException;
import com.scaling.libraryservice.repository.LibraryRepository;
import com.scaling.libraryservice.util.OpenApiQuerySender;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class MapBookServiceTest {

    @Autowired
    private MapBookService mapBookService;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private OpenApiQuerySender sender;

    /*@BeforeEach
    public void setUp(){

        this.sender  = new OpenApiQuerySender(new RestTemplate());


        *//*Mockito.when(libraryRepository.findLibInfo("성남")).thenReturn();*//*
    }*/


    @Test @DisplayName("지역 도서관 대출 가능 여부 데이터 생성")
    public void exist_book_with_location(){
        /* given */

        String isbn = "9788994492032";
        String area = "성남";

        /* when */

        List<RespBookMapDto> result = mapBookService.loanAbleLibrary(isbn,area);

        /* then */


        assertNotEquals(0,result.size());
        result.forEach(System.out::println);
    }





}