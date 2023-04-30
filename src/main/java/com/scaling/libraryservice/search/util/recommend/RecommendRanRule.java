package com.scaling.libraryservice.search.util.recommend;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.RecommendBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Timer
public class RecommendRanRule implements RecommendRule {

    @Override
    public RespRecommend recommendBooks(RespBooksDto searchResult) {
        List<BookDto> document = searchResult.getDocuments();

        // 최소 10권 결과일때도 결과값 나오도록 처리
        int limit = Math.max(document.size(), 10);

        // 상위 limit권
        List<RecommendBookDto> relatedBooks = getTopRelatedBooks(document, limit);

        // 10권 랜덤 추천
        List<RecommendBookDto> randomTop10 = getRandomTop10RelatedBooks(relatedBooks);



        return new RespRecommend(randomTop10);


    }

    // 상위 100권
    private List<RecommendBookDto> getTopRelatedBooks(List<BookDto> document, int limit) {
        return document.stream()
            .filter(bookDto -> bookDto.getTitle() != null)
            .map(bookDto -> new RecommendBookDto(bookDto.getRelatedTitle()))
            .distinct()
            .limit(limit)
            .collect(Collectors.toList());
    }


    // 랜덤으로 10권 선택
    public List<RecommendBookDto> getRandomTop10RelatedBooks(List<RecommendBookDto> relatedBooks) {
        Collections.shuffle(relatedBooks);
        List<RecommendBookDto> relatedBookDtos = relatedBooks.stream()
            .limit(10)
            .collect(Collectors.toList());
        return relatedBookDtos;
    }


}
