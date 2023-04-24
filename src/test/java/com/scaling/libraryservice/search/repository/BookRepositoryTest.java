package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        query = Arrays.stream(query.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));

        /* when */
        var result = bookRepos.findBooksByEnglish(query);


        /* then */
        System.out.println(result);
        assertNotEquals(0,result.size());
    }
    
    @Test
    public void english_korean_tilte(){
        /* given */
        //e-mail에 꼭 필요한 알짜표현!! 3600=(A)handbook of business letter writing

        String query = "email 알짜표현";
        
        /* when */

        System.out.println(bookRepos.findBooksByEngAndKor("email","알짜표현"));
    
        /* then */
    }

}