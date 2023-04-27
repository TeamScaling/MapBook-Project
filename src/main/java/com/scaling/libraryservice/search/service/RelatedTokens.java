package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.dto.MetaDto;
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
public class RelatedTokens {

    private final RelatedBooks relatedBooks;

    public RespBooksDto RelatedTokens(RespBooksDto searchResult) {

        List<RelatedBookDto> relatedBooks = this.relatedBooks.relatedBooks(searchResult).getRelatedBooks();
        TokenDto tokenDto = processRelatedBooks(relatedBooks);

        return new RespBooksDto(new MetaDto(), Collections.emptyList(), null, tokenDto);
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
