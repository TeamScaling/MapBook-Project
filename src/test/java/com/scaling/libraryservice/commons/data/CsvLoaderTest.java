package com.scaling.libraryservice.commons.data;

import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class CsvLoaderTest {

    @Autowired
    CsvLoader<BookVo> csvLoader;

    @Autowired
    TitleAnalyzer titleAnalyzer;

    @Autowired
    CsvWriter csvWriter;

    @Autowired
    BookRepoQueryDsl bookRepository;

    @Test
    public void test2() {
        String path = "C:\\Users\\dignz\\IdeaProjects\\LibraryService\\books.tsv";

        /* when */

        var result = csvLoader.loadBooksFromCsv(path, BookVo.class);
        System.out.println(result.size());
    }

    @Test
    public void test3() {

        int pageSize = 500000;
        int pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Book> page;
        do {
            page = bookRepository.findAllAndSort(pageable);

            List<BookVo2> books = new ArrayList<>();
            List<BookVo2> engBooks = new ArrayList<>();

            for (Book book : page.getContent()) {
                TitleQuery query = titleAnalyzer.analyze(book.getTitle());

                StringJoiner joiner = new StringJoiner(" ");

                Arrays.stream(query.getEngKorTokens().split(" "))
                    .distinct().forEach(joiner::add);

                books.add(new BookVo2(book, joiner.toString()));
            }

            csvWriter.writeAnalyzedBooksToCsv(books, "bookFinal2.csv");

            pageable = pageable.next();  // 다음 페이지를 가져오기 위한 설정
            System.out.println("hello");
        } while (page.hasNext());  // 다음 페이지가 존재하는지 확인
    }

    @Test
    public void test4(){
        /* given */

        TitleQuery query = titleAnalyzer.analyze("스프링 시큐리티 3 :스프링 프레임워크 기반 표준 보안 솔루션");

        StringJoiner joiner = new StringJoiner(" ");

        Arrays.stream(query.getKorToken().split(" "))
            .distinct().forEach(joiner::add);

        System.out.println(joiner.toString());

        /* when */

        /* then */
    }

}