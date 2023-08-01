package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.entity.Book;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class BookRepoQueryDslTest {

    @Autowired
    BookRepoQueryDsl bookRepoQueryDsl;
    
    @Test
    public void findBooksByIsbn_not_empty(){
        /* given */

        String isbn = "9788972756194";
        
        /* when */

        BookDto book = bookRepoQueryDsl.findBooksByIsbn(isbn);
    
        /* then */

        assertNotEquals(book.getTitle(), "");
    }

    @Test
    public void findBooksByIsbn_empty(){
        /* given */

        String isbn = "97889727561";

        /* when */

        BookDto book = bookRepoQueryDsl.findBooksByIsbn(isbn);

        /* then */

        assertEquals(book.getTitle(), "");
    }

}