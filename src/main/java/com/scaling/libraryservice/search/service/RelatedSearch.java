package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.mapBook.entity.Ranks;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.QueryDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.RelatedSearchRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatedSearch {
    private final RelatedSearchRepository relatedSearchRepository;

    public List<String> findRelatedRanks(List<BookDto> document) {
        Map<String, Integer> countMap = new HashMap<>();

        for (BookDto book : document) {
            String kdcNm = book.getKdcNm();
            if (kdcNm != null && kdcNm.matches("\\d+\\.\\d+")) { // 숫자 패턴 체크
                String[] splitKdcNm = kdcNm.split("\\.");
                String firstDigit = kdcNm.substring(0, 5); // 첫 번째 2글자 추출
                System.out.println(firstDigit);


                countMap.put(firstDigit, countMap.getOrDefault(firstDigit, 0) + 1);
            }
        }

        String mostFrequentFirstDigit = Collections.max(countMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        String firstDigit = String.valueOf(mostFrequentFirstDigit.charAt(0));
        System.out.println("가장 많은 숫자: " + mostFrequentFirstDigit);
        System.out.println("가장 많은 숫자의 맨 앞 자리: " + firstDigit);

        //rank에서 같은 번호대의 책들 책들 가져오기

        List<String> relatedRanks = relatedSearchRepository.findRelatedQuery(mostFrequentFirstDigit);

        return relatedRanks;
    }




}
