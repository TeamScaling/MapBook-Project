package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.util.relate.RelationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelationService {

    private final RelationRule relationRule;

    public RespBooksDto getRelatedBooks(RespBooksDto reference){

        return relationRule.relatedBooks(reference);
    }

}
