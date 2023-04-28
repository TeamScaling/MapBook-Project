package com.scaling.libraryservice.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespRelationDto {

    private final List<RelatedBookDto> relatedBooks;

    public RespRelationDto(List<RelatedBookDto> relatedBooks) {
        this.relatedBooks = relatedBooks;
    }
}
