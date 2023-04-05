package com.scaling.libraryservice.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespBooksDto {

    private MetaDto meta;
    private List<BookDto> documents;

    public RespBooksDto(MetaDto metaDto,List<BookDto> documents) {
        this.meta = metaDto;
        this.documents = documents;
    }
}
