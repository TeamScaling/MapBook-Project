package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.TokenDto;
import com.scaling.libraryservice.search.util.KorTokenizer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Timer
public class RelatedContents {

    public RespBooksDto relatedContents(RespBooksDto searchResult) {
        List<BookDto> document = searchResult.getDocuments();

        // 최소 10권 결과일때도 결과값 나오도록 처리
        int limit = Math.max(document.size(), 10);

        // 상위 limit권
        List<RelatedBookDto> relatedBooks = getTopRelatedBooks(document, limit);

        // 10권 랜덤 추천
        List<RelatedBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);

        // 토크나이저로 연관검색어 추출
        TokenDto tokenDto = processRelatedBooks(relatedBooks);

        return new RespBooksDto(searchResult.getMeta(), searchResult.getDocuments(), randomTop10, tokenDto);

    }

    // 추천책 상위 100권
    private List<RelatedBookDto> getTopRelatedBooks(List<BookDto> document, int limit) {
        return document.stream()
            .filter(bookDto -> bookDto.getTitle() != null)
            .map(bookDto -> new RelatedBookDto(bookDto.getRelatedTitle()))
            .distinct()
            .limit(limit)
            .collect(Collectors.toList());
    }


    // 랜덤으로 10권 선택
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
