package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.dto.BookDto;
import com.scaling.libraryservice.entity.Book;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Nested
    class SearchTitle {

        @Test
        @DisplayName("검색 단어 위치와 상관 없이 검색이 되는가")
        public void findBooksByTitle() {
            /* given */
            String title1 = "자바 정석";
            String title2 = "정석 자바";


            /* when */
            List<Book> books1 = bookRepository.findBooksByTitle(title1);
            List<Book> books2 = bookRepository.findBooksByTitle(title2);

            Book book1 = books1
                .stream()
                .filter(book -> book.getSeqId().equals(3123163))
                .findFirst()
                .get();

            Book book2 = books1
                .stream()
                .filter(book -> book.getSeqId().equals(3123163))
                .findFirst()
                .get();
            //.toString()을 사용해도됨

            /* then */
            //9788994492032
            //3123163
            Assertions.assertEquals(book1, book2);


        }
    }


}
