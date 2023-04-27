package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.TokenDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.NGram;
import com.scaling.libraryservice.search.util.TitleDivider;
import com.scaling.libraryservice.search.util.TitleTokenizer;
import java.util.ArrayList;
import com.scaling.libraryservice.search.util.KorTokenizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
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
import org.springframework.util.StopWatch;


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

 //==========================작업중==============================================>

            // 최소 10권 결과일때도 결과값 나오도록 처리
            int limit = Math.max(document.size(), 10);

            // 상위 limit권
            List<RelatedBookDto> relatedBooks = getTopRelatedBooks(document, limit);

            // 10권 랜덤 추천
            List<RelatedBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);

            //토크나이저로 연관검색어 추출
            TokenDto tokenDto = processRelatedBooks(relatedBooks);

//=========================작업끝===============================================>
            MetaDto meta
                = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

            return new RespBooksDto(meta, document,randomTop10,tokenDto);

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
            return bookRepository.findBooksByTitleFlexible(query, pageable);
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
//==========================작업중==============================================>

        // 최소 10권 결과일때도 결과값 나오도록 처리
        int limit = Math.max(document.size(), 10);

        // 상위 limit권
        List<RelatedBookDto> relatedBooks = getTopRelatedBooks(document, limit);

        // 10권 랜덤 추천
        List<RelatedBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);

        //토크나이저로 연관검색어 추출
        TokenDto tokenDto = processRelatedBooks(relatedBooks);

//=========================작업끝===============================================>


        MetaDto meta
            = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document,randomTop10,tokenDto);
    }

    private RespBooksDto searchBooksInKorean2(String query, int page, int size, String target) {
        Pageable pageable = createPageable(page, size);

        Page<Book> books;

        if (query.split(" ").length == 1) {
            books = bookRepository.findBooksByOneTitleFlexible(query, pageable);

        } else {
            books = findBooksByTarget(splitTarget(query), pageable, target);
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

    private RespBooksDto searchBooksInEnglish(String query, Pageable pageable, int page, int size) {

        Page<Book> books = bookRepository.findBooksByEnglishTitleNormal(query, pageable);

        Objects.requireNonNull(books);

        List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();

//==========================작업중==============================================>

        // 최소 10권 결과일때도 결과값 나오도록 처리
        int limit = Math.max(document.size(), 10);

        // 상위 limit권
        List<RelatedBookDto> relatedBooks = getTopRelatedBooks(document, limit);

        // 10권 랜덤 추천
        List<RelatedBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);

        // 토크나이저로 연관검색어 추출
        TokenDto tokenDto = processRelatedBooks(relatedBooks);

//=========================작업끝===============================================>

        MetaDto meta
            = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document,randomTop10,tokenDto);

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

        //"퍼펙트 EJB";
        Pageable pageable = createPageable(page, size);

        Page<Book> books;
        MetaDto meta;

        log.info("isEnglish : " + isEnglish(query));

        if (isEnglish(query)) {

            log.info("english title : [{}]",query);

            query = "+"+query;

            books = bookRepository.findBooksByEngTitleFlexible(query, pageable);

        } else if (isKorean(query)) {

            log.info("korean title : [{}]",query);

            return searchBooksInKorean2(query, page, size, target);
        } else {

            Map<String, List<String>> titleMap = TitleDivider.divideTitle(query);

            List<String> engTokens = titleMap.get("eng");
            List<String> korTokens = titleMap.get("kor");

            String korToken = String.join(" ", korTokens);
            String engToken = String.join(" ", engTokens);

            StringBuilder builder = new StringBuilder();

            engTokens.forEach(t -> builder.append("%").append(t).append("% "));

            log.info("korToken : {}, length : {}",korToken,korToken.length());
            log.info("engToken : {}, length : {}",engToken,engToken.length());

            if (korToken.length() >= engToken.length()) {

                log.info("korToken >= engToken");

                books = bookRepository.findBooksByTitleNmode(query, pageable);

            } else {

                if (!korToken.isEmpty()) {
                    List<String> nnKorTokens = titleTokenizer.tokenize(korToken);
                    korToken = String.join(" ", nnKorTokens);
                }

                log.info("[korToken < engToken] korToken : {} // engToken : {}", korToken,
                    engToken);

                books = bookRepository.findBooksByEngAndKor(builder.toString().trim(), korToken,
                    pageable);
            }


        }


//==========================작업중==============================================>
        List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();

        // 최소 10권 결과일때도 결과값 나오도록 처리
        int limit = Math.max(document.size(), 10);

        // 상위 limit권
        List<RelatedBookDto> relatedBooks = getTopRelatedBooks(document, limit);

        // 10권 랜덤 추천
        List<RelatedBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);

        // 토크나이저로 연관검색어 추출
        TokenDto tokenDto = processRelatedBooks(relatedBooks);

//==========================작업중==============================================>


        meta = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document,randomTop10,tokenDto);



    }




    // 추천책 상위 100권
    @Timer
    private List<RelatedBookDto> getTopRelatedBooks(List<BookDto> document, int limit) {
        return document.stream()
            .filter(bookDto -> bookDto.getTitle() != null)
            .map(bookDto -> new RelatedBookDto(bookDto.getRelatedTitle()))
            .distinct()
            .limit(limit)
            .collect(Collectors.toList());
    }


    // 랜덤으로 10권 선택
    @Timer
    public List<RelatedBookDto> getRandomTop10RelatedBooks(List<RelatedBookDto> relatedBooks) {
        Collections.shuffle(relatedBooks);
        List<RelatedBookDto> relatedBookDtos = relatedBooks.stream()
            .limit(10)
            .collect(Collectors.toList());
        return relatedBookDtos;
    }

    // 연관검색어 명사 추출 한글
    public TokenDto processRelatedBooks(List<RelatedBookDto> relatedBooks) {
        KorTokenizer tokenizer = new KorTokenizer(new Komoran(DEFAULT_MODEL.FULL));
        List<String> nouns = relatedBooks.stream()
            .flatMap(relatedBookDto -> tokenizer.tokenize(relatedBookDto.getTitle()).stream())
            .collect(Collectors.toList());

        Map<String, Long> countedNouns = nouns.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

//        List<String> token = countedNouns.entrySet().stream()
//            .filter(entry -> entry.getKey().length() >= 2 && entry.getValue() >= 1)
//            .map(Map.Entry::getKey)
//            .limit(10)
//            .collect(Collectors.toList());


        // 가장 많이 나온 단어가 앞쪽에 나오도록 정렬
        List<String> token = countedNouns.entrySet().stream()
            .filter(entry -> entry.getKey().length() >= 2 && entry.getValue() >= 1)
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .limit(10)
            .collect(Collectors.toList());


        log.info("filteredNouns : " + token);

        TokenDto tokenDto = new TokenDto(token);
        return tokenDto;
    }

}





