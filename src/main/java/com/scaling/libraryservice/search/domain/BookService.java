package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.PerformanceMeasurement;
import com.scaling.libraryservice.search.dto.SearchResponseDto;
import com.scaling.libraryservice.search.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {
    // AOP
    private final BookRepository bookRepository;
    private final PerformanceMeasurement measurement = new PerformanceMeasurement();

    @Transactional(readOnly = true)
    public SearchResponseDto searchBook(String title_nm){
        if(title_nm.equals("") || title_nm.equals(" ")){
            throw new IllegalArgumentException("검색어를 입력하세요.");
        }
        measurement.start();

        List<Book> documents = bookRepository.findBooksByTitle_nm(title_nm);

        int bookCnt = documents.size();

        System.out.println(bookCnt);

        measurement.end();
        long elapsedTime = measurement.getElapsedTime();

        System.out.println("작업에 소요된 시간: " + elapsedTime + "ms");

        return new SearchResponseDto(documents, bookCnt);
    }
}
