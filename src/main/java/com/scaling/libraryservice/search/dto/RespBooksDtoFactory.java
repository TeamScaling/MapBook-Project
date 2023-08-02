package com.scaling.libraryservice.search.dto;

import static com.scaling.libraryservice.search.dto.MetaDtoFactory.*;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;

public class RespBooksDtoFactory {

    public static RespBooksDto createDefaultRespBooksDto(Page<BookDto> books, ReqBookDto reqBookDto) {
        return new RespBooksDto(new MetaDto(books, reqBookDto), books);
    }

    public static RespBooksDto createEmptyRespBookDto(String userQuery) {
        return new RespBooksDto(createEmptyDto(userQuery), Collections.emptyList());
    }

    public static RespBooksDto createIsbnRespBookDto(BookDto bookDto,String userQuery) {
        return new RespBooksDto(createIsbnMetaDto(userQuery), List.of(bookDto));
    }

    public static RespBooksDto createSessionRespBookDto(MetaDto metaDto, BookDto bookDto) {
        return new RespBooksDto(createSessionMetaDto(metaDto.getSearchTime()), List.of(bookDto));
    }

    public static RespBooksDto createOneBookRespDto(String userQuery, BookDto bookDto) {
        return new RespBooksDto(createOneMetaDto(userQuery), List.of(bookDto));
    }
}
