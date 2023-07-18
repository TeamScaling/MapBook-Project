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
class CsvWriterTest {

    @Autowired
    TitleAnalyzer titleAnalyzer;

    @Autowired
    CsvWriter csvWriter;

    @Autowired
    BookRepoQueryDsl bookRepository;

    public void test3() {

        int pageSize = 500000;
        int pageNumber = 0;

        // 360만건 도서 데이터이기 때문에 한번에 모든 데이터를 가져오면
        // out of memory 문제가 일어나 paging을 통해 나눠서 가져온다.

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Book> page;
        do {
            // 대출 횟수(loan_cnt) 기준으로 내림 차순으로 데이터를 입력하고,
            // 실제 작업을 수행하기 위해 DB에서 Java단으로 데이터를 가져온다
            page = bookRepository.findAllAndSort(pageable);

            List<BookVo2> books = new ArrayList<>();

            for (Book book : page.getContent()) {

                // 도서 제목을 한글과 영어 단어로 분리하고, 한글은 명사 단위로 추출,
                // 영어는 모두 소문자로 변환 한다.

                TitleQuery query = titleAnalyzer.analyze(book.getTitle());

                StringJoiner joiner = new StringJoiner(" ");

                Arrays.stream(query.getEngKorTokens().split(" "))
                    .distinct().forEach(joiner::add);

                books.add(new BookVo2(book, joiner.toString()));
            }

            //마지막으로 객체를 변환하여 Csv file로 만들기 위해
            //미리 정의한 메소드를 호출 한다
            csvWriter.writeAnalyzedBooksToCsv(books, "bookFinal2.csv");

            pageable = pageable.next();  // 다음 페이지를 가져오기 위한 설정

        } while (page.hasNext());  // 다음 페이지가 존재하는지 확인
    }

}