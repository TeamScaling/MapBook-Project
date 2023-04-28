package com.scaling.libraryservice.search.util.recommend;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Timer
public class RecommendRanRule implements RecommendRule {

    @Override
    public RespBooksDto recommendBooks(RespBooksDto searchResult) {
        List<BookDto> document = searchResult.getDocuments();

        // 최소 10권 결과일때도 결과값 나오도록 처리
        int limit = Math.max(document.size(), 10);

        // 상위 limit권
        List<RelatedBookDto> relatedBooks = getTopRelatedBooks(document, limit);

        // 10권 랜덤 추천
        List<RelatedBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);


        return new RespBooksDto(searchResult.getMeta(), searchResult.getDocuments(), randomTop10, null);


    }

    // 상위 100권
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


}
