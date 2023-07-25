package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordRepository keywordRepo;
    public boolean isExistKeyword(String query){
        return keywordRepo.existsKeywordByKeyword(query);
    }

}
