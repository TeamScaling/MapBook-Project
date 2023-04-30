package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.cacheKey.BookCacheKey;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString
public class RespBooksDto {

    private MetaDto meta;
    private List<BookDto> documents;

    private BookCacheKey bookCacheKey;

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents) {
        this.meta = metaDto;
        this.documents = documents;
    }



}