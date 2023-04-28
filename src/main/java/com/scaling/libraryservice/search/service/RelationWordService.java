package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.RecommendBookDto;
import com.scaling.libraryservice.search.dto.RelationWords;
import com.scaling.libraryservice.search.util.relate.RelationRule;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelationWordService {

    private final RelationRule relationRule;

    @Timer
    public RelationWords getRelatedWords(List<RecommendBookDto> recommendBooks){

        return relationRule.relatedBooks(recommendBooks);
    }

}
