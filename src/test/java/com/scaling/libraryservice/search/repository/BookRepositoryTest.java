package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepos;

    @Autowired
    private BookSearchService bookSearchService;

    @Test
    public void only_english_title(){
        /* given */

        String query = "spring boot";

        Pageable pageable = bookSearchService.createPageable(1,10);

        query = Arrays.stream(query.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));

        /* when */
        var result = bookRepos.findBooksByEngNatural("spring boot",pageable);


        /* then */
        System.out.println(result.getContent());
        assertNotEquals(0,result.getContent().size());
    }
    
    @Test
    public void english_korean_title(){
        /* given */
        //e-mail에 꼭 필요한 알짜표현!! 3600=(A)handbook of business letter writing

        String query = "(가장 쉽게 배우는)볼랜드 C++ Builder 5";
        Pageable pageable = bookSearchService.createPageable(1,10);
        
        /* when */

        var result = bookRepos.findBooksByEngKorNatural("%C++%","볼랜드",pageable);

        /* then */
        System.out.println(result.getContent());

        assertNotEquals(0,result.getContent().size());
    }

    @Test
    public void english_korean_title2(){
        /* given */
        //e-mail에 꼭 필요한 알짜표현!! 3600=(A)handbook of business letter writing

        String query = "퍼펙트 EJB";

        Pageable pageable = bookSearchService.createPageable(1,10);
        /* when */

        query = Arrays.stream(query.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));

        var result = bookRepos.findBooksByEngKorBool("%EJB%","퍼펙트",pageable);

        /* then */
        System.out.println(result.getContent());

        assertNotEquals(0,result.getContent().size());
    }

}