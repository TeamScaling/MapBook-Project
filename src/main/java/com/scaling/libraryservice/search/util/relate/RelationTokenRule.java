package com.scaling.libraryservice.search.util.relate;

import com.scaling.libraryservice.recommend.dto.RecommendBookDto;
import com.scaling.libraryservice.search.dto.RelationWords;
import com.scaling.libraryservice.search.util.KorTokenizer;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RelationTokenRule implements RelationRule {


    @Override
    public RelationWords relatedBooks(List<RecommendBookDto> recommendBooks) {


        return processRelatedBooks(recommendBooks);
    }

    // 연관검색어 명사 추출 한글
    public RelationWords processRelatedBooks(List<RecommendBookDto> relatedBooks) {
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

        RelationWords relationWords = new RelationWords(token);
        return relationWords;
    }


}
