package com.scaling.libraryservice.dataPipe.csv.exporter;

import com.scaling.libraryservice.dataPipe.csv.util.CsvWriter;
import com.scaling.libraryservice.dataPipe.vo.BookVo;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.engine.filter.SimpleFilter;
import com.scaling.libraryservice.search.engine.filter.StopWordFilter;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BookExporter extends ExporterService<BookVo, Book> {

    private final TitleAnalyzer titleAnalyzer;
    private final BookRepoQueryDsl bookRepository;
    private Page<Book> page;

    public BookExporter(CsvWriter<BookVo> csvWriter,
        TitleAnalyzer titleAnalyzer, BookRepoQueryDsl bookRepository) {

        super(csvWriter);
        this.titleAnalyzer = titleAnalyzer;
        this.bookRepository = bookRepository;
        this.page = Page.empty();
    }

    @Override
    List<BookVo> exportVoWithOption(Pageable pageable, String outputName, boolean option) {
        // 대출 횟수(loan_cnt) 기준으로 내림 차순으로 데이터를 입력하고,
        // 실제 작업을 수행하기 위해 DB에서 Java단으로 데이터를 가져온다
        this.page = bookRepository.findAllBooks(pageable);
        List<BookVo> books = new ArrayList<>();

        page.stream().forEach(book -> {
            if (option) {
                TitleQuery query = titleAnalyzer.analyze(book.getTitle(), false);
                String distinctToken = distinctToken(query.getNnToken());

                books.add(new BookVo(book, distinctToken));
            } else {
                String titleToken = authorAndTitleToken(book);
                books.add(new BookVo(titleToken, book));
            }
        });

        pageable.next();  // 다음 페이지를 가져오기 위한 설정

        return books;
    }

    // 기존 title_token에 저자를 합친다.
    private String authorAndTitleToken(Book book) {
        SimpleFilter simpleFilter = new SimpleFilter(new StopWordFilter(null));

        String titleTokens = book.getTitleToken().isBlank() ? "" : book.getTitleToken();
        String author = simpleFilter.filtering(book.getAuthor());

        Set<String> distinctTokens = Stream.of(titleTokens, author)
            .flatMap(token -> Arrays.stream(token.split(" ")))
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return String.join(" ", distinctTokens);
    }

    private String distinctToken(String query) {

        return Arrays.stream(query.split(" "))
            .distinct()
            .collect(Collectors.joining(" "));
    }


    public Page<Book> renewPage() {
        return page;
    }

}
