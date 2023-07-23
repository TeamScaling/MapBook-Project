package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.commons.data.BookVo2;
import com.scaling.libraryservice.commons.data.CsvWriter;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.util.TitleAnalyzer;
import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookExportService {

    private final BookRepoQueryDsl bookRepository;
    private final CsvWriter csvWriter;
    private final TitleAnalyzer titleAnalyzer;

    public void analyzeAndExportBooks(int pageNumber,int pageSize,String outputName){

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
                // 도세 제목에서 영어 단어와 한글 명사 단어를 추출 한다.
                TitleQuery query = titleAnalyzer.analyze(book.getTitle());
                books.add(new BookVo2(book, String.join(" ",query.getNnToken())));
            }

            //마지막으로 객체를 변환하여 Csv file로 만들기 위해
            //미리 정의한 메소드를 호출 한다
            csvWriter.writeAnalyzedBooksToCsv(books, outputName);
            pageable = pageable.next();  // 다음 페이지를 가져오기 위한 설정

        } while (page.hasNext());  // 다음 페이지가 존재하는지 확인
    }

}
