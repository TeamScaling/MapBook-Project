package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.entity.Keyword;
import com.scaling.libraryservice.search.repository.KeywordQueryDsl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordQueryDsl keywordQueryDsl;

    public List<String> getExistKeywords(List<String> requiredCheckWords) {
        return keywordQueryDsl.getKeywords(requiredCheckWords.toArray(String[]::new))
            .stream()
            .map(Keyword::getKeyword)
            .toList();
    }
}
