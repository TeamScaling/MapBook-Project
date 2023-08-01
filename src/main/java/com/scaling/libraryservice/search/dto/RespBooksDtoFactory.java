package com.scaling.libraryservice.search.dto;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;

public class RespBooksDtoFactory {

    public static RespBooksDto createDefaultRespBooksDto(Page<BookDto> books, ReqBookDto reqBookDto) {
        return new RespBooksDto(new MetaDto(books, reqBookDto), books);
    }

    public static RespBooksDto createEmptyRespBookDto(String userQuery) {
        return new RespBooksDto(MetaDto.emptyDto(userQuery), Collections.emptyList());
    }

    public static RespBooksDto createIsbnRespBookDto(BookDto bookDto,String userQuery) {
        return new RespBooksDto(MetaDto.isbnMetaDto(userQuery), List.of(bookDto));
    }

    public static RespBooksDto createSessionRespBookDto(MetaDto metaDto, BookDto bookDto) {
        return new RespBooksDto(MetaDto.sessionMetaDto(metaDto.getSearchTime()), List.of(bookDto));
    }

    public static RespBooksDto createOneBookRespDto(String userQuery, BookDto bookDto) {
        return new RespBooksDto(MetaDto.oneMetaDto(userQuery), List.of(bookDto));
    }
}
