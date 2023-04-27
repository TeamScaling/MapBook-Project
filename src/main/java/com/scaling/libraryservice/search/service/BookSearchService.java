package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.NGram;
import com.scaling.libraryservice.search.util.TitleDivider;
import com.scaling.libraryservice.search.util.TitleTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookSearchService {

    private final BookRepository bookRepository;

    private final TitleTokenizer titleTokenizer;

    // 도서 검색
    @Timer
    public RespBooksDto searchBooks(String query, int page, int size, String target) {
        Pageable pageable = createPageable(page, size);

        if (isEnglish(query)) {

            String[] split = query.split("\\s+");

            List<String> filteredWords = Arrays.stream(split)
                .filter(word -> word.length() > 6)
                .toList();

            if (filteredWords.isEmpty()) {

                return searchBooksInEnglish(query, pageable, size, page);

            }

            String parsedQuery = parsedQuery(query);

            Page<Book> books = bookRepository.findBooksByEnglishTitleNormal(parsedQuery, pageable);

            Objects.requireNonNull(books);

            List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();

            MetaDto meta
                = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

            return new RespBooksDto(meta, document);

        }

        return searchBooksInKorean(query, page, size, target);

    }

    // pageable 객체에 값 전달
    public Pageable createPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    // target에 따라 쿼리 선택하여 동적으로 변동
    private Page<Book> findBooksByTarget(String query, Pageable pageable, String target) {
        if (target.equals("author")) {
            return bookRepository.findBooksByAuthor(query, pageable);
        } else if (target.equals("title")) {
            return bookRepository.findBooksByKorMtFlexible(query, pageable);
        } else {
            return null; //api 추가될 것 고려하여 일단 Null로 넣어놓음
        }
    }

    // 띄어쓰기 전처리
    private String splitTarget(String target) {
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));
    }

    //dto 리스트에서 참조변수에 값 전달
    private List<BookDto> convertToBookDtoList(Page<Book> books) {
        return books.getContent()
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());
    }


    private RespBooksDto searchBooksInKorean(String query, int page, int size, String target) {
        Pageable pageable = createPageable(page, size);

        Page<Book> books = findBooksByTarget(splitTarget(query), pageable, target);

        Objects.requireNonNull(books);

        List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();

        MetaDto meta
            = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document);
    }

    private RespBooksDto searchBooksInEnglish(String query, Pageable pageable, int page, int size) {

        Page<Book> books = bookRepository.findBooksByEnglishTitleNormal(query, pageable);

        Objects.requireNonNull(books);

        List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();

        MetaDto meta
            = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document);

    }

    String parsedQuery(String query) {
        List<String> ngramList = NGram.getNGrams(query.replaceAll("\\s", ""), 4);

        String[] fields = {"ENG_TITLE_NM"};
        QueryParser parser = new QueryParser(fields[0], new StandardAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.OR);
        parser.setMaxDeterminizedStates(1024); // 최대 절 개수를 1024로 제한

        Query parsedQuery = null;
        try {
            parsedQuery = parser.parse(QueryParser.escape(String.join(" ", ngramList)));
        } catch (ParseException e) {

            log.error(e.getMessage());
        }
        return String.valueOf(parsedQuery);
    }

    public static boolean isEnglish(String input) {
        String pattern = "^[a-zA-Z0-9\\.\\s]+$";
        return input.matches(pattern);
    }

    public static boolean isKorean(String input) {
        String pattern = "^[가-힣0-9\\.\\s]+$";
        return input.matches(pattern);
    }

    @Timer
    public RespBooksDto searchBooks2(String query, int page, int size, String target) {

        Pageable pageable = createPageable(page, size);

        Page<Book> books;
        MetaDto meta;

        if (isEnglish(query)) {

            log.info("english title : [{}]", query);

            return queryResolve(query, page, size, target, false);

        } else if (isKorean(query)) {

            log.info("korean title : [{}]", query);

            return queryResolve(query, page, size, target, true);
        } else {
            log.info("korean & english title : [{}]", query);
            books = engKorResolve(query, pageable);
        }

        meta = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta
            , books.stream().map(BookDto::new).toList());
    }

    private RespBooksDto queryResolve(String query, int page, int size, String target,
        boolean isKor) {
        Pageable pageable = createPageable(page, size);

        Page<Book> books;

        if (query.split(" ").length == 1) {

            if (isKor) {
                log.info("Single Kor query : [{}]", query);
                books = bookRepository.findBooksByKorNatural(query, pageable);
            } else {
                log.info("Single Eng query : [{}]", query);
                books = bookRepository.findBooksByEngNatural(query, pageable);
            }

        } else {

            if (isKor) {
                query = splitTarget(query);
                log.info("Multi Kor query : [{}]", query);
                books = bookRepository.findBooksByKorMtFlexible(query, pageable);
            } else {
                query = splitTarget(query);
                log.info("Multi Eng query : [{}]", query);
                books = bookRepository.findBooksByEngMtFlexible(query, pageable);
            }
        }

        List<BookDto> document;

        if (books != null) {
            document = books.getContent().stream().map(BookDto::new).toList();
            MetaDto meta
                = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

            return new RespBooksDto(meta, document);
        } else {

            return new RespBooksDto(
                new MetaDto(),
                new ArrayList<>());
        }
    }

    private Page<Book> engKorResolve(String query, Pageable pageable) {

        Map<String, List<String>> titleMap = TitleDivider.divideTitle(query);

        List<String> engTokens = titleMap.get("eng");
        List<String> korTokens = titleMap.get("kor");

        String korToken = String.join(" ", korTokens);
        String engToken = String.join(" ", engTokens);

        StringBuilder engQueryBuilder = new StringBuilder();

        engTokens.forEach(t -> engQueryBuilder.append("%").append(t).append("% "));

        log.info("korToken : {}, length : {}", korToken, korToken.length());
        log.info("engToken : {}, length : {}", engToken, engToken.length());

        if (korToken.length() >= engToken.length()) {

            log.info("korToken >= engToken");

            return bookRepository.findBooksByKorNatural(query, pageable);

        } else {

            List<String> nnKorTokens = new ArrayList<>();

            // 한글 제목 내용을 명사 단위로만 검색 한다.
            if (!korToken.isEmpty()) {
                nnKorTokens = titleTokenizer.tokenize(korToken);
                korToken = String.join(" ", nnKorTokens);
            }

            if(nnKorTokens.size() <=1){
                korToken = splitTarget(korToken);

                log.info("[korToken < engToken] korToken : {} // engToken : {}", korToken,
                    engToken);


                return bookRepository.findBooksByEngKorBool(
                    engQueryBuilder.toString().trim(),
                    korToken,
                    pageable);
            }else{

                return bookRepository.findBooksByEngKorNatural(
                    engQueryBuilder.toString().trim(),
                    korToken,
                    pageable);
            }
        }

    }

}





