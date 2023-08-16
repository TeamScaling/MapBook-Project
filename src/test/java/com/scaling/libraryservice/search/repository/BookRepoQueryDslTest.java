package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.TestConfig;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.engine.TitleQuery.TitleQueryBuilder;
import com.scaling.libraryservice.search.engine.TitleType;
import com.scaling.libraryservice.search.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import(TestConfig.class)
class BookRepoQueryDslTest {

    @Autowired
    BookRepoQueryDsl bookRepoQueryDsl;

    @Test @DisplayName("명사가 두개 이상인 사용자 검색어를 통해 DB에서 도서를 검색 할 수 있다")
    public void findBooks() {
        /* given */

        TitleQuery titleQuery = new TitleQueryBuilder().userQuery("java 정석")
            .nnToken("java 정석")
            .etcToken("")
            .titleType(TitleType.TOKEN_TWO_OR_MORE).build();

        Pageable pageable = Pageable.ofSize(10);

        /* when */

        Page<BookDto> books = bookRepoQueryDsl.findBooks(titleQuery, pageable);


        /* then */

        assertFalse(books.getContent().isEmpty());
    }

//    @Test @DisplayName("조건 없이 페이지 사이즈 만큼 DB에서 도서 데이터를 가져올 수 있다.")
//    public void findAllBooksWithoutCondition(){
//        /* given */
//
//        Pageable pageable = PageRequest.of(0,10);
//
//        /* when */
//
//        Page<Book> booksWithoutCondition = bookRepoQueryDsl.findBooks(pageable,1000L);
//
//        /* then */
//
//        assertEquals(booksWithoutCondition.getContent().size(),10);
//    }

//    @Test @DisplayName("조건 없이 페이지 사이즈 만큼 DB에서 정렬된 도서 데이터를 가져올 수 있다.")
//    public void findAllAndSort(){
//        /* given */
//
//        Pageable pageable = PageRequest.of(0,10);
//
//        /* when */
//
//        Page<Book> books = bookRepoQueryDsl.findAllAndSort(pageable,100L);
//
//        Book book1 = books.getContent().get(0);
//        Book book2 = books.getContent().get(1);
//
//        /* then */
//
//        assertEquals(books.getContent().size(),10);
//        assertTrue(book1.getLoanCnt() > book2.getLoanCnt());
//    }
//
//    @Test @DisplayName("제목이 명사로 쪼개진 칼럼인 TitleToken에서 데이터를 가져올 수 있다")
//    public void findTitleToken(){
//        /* given */
//
//        Pageable pageable = PageRequest.of(0,10);
//
//        /* when */
//
//        Page<String> books = bookRepoQueryDsl.findTitleToken(pageable,100L);
//
//        /* then */
//
//        assertFalse(books.getContent().isEmpty());
//    }

    @Test
    @DisplayName("ISBN으로 원하는 도서를 찾을 수 있다.")
    public void findBooksByIsbn_not_empty() {
        /* given */

        String isbn = "9788937460005";
        String expectTitle = "태엽 감는 새 연대기 ";

        /* when */

        BookDto book = bookRepoQueryDsl.findBooksByIsbn(isbn);

        /* then */

        assertEquals(book.getTitle(), expectTitle);
    }

    @Test
    public void findBooksByIsbn_empty() {
        /* given */

        String isbn = "97889727561";

        /* when */

        BookDto book = bookRepoQueryDsl.findBooksByIsbn(isbn);

        /* then */

        assertEquals(book.getTitle(), "");
    }

}