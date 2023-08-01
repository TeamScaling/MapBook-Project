package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.dto.MetaDto.Builder;
import org.springframework.data.domain.Page;

public class MetaDtoFactory {

    public static MetaDto createDefaultMetaDto(Page<BookDto> fetchedBooks, ReqBookDto reqBookDto){
        return new MetaDto(fetchedBooks, reqBookDto);
    }

    public static MetaDto createIsbnMetaDto(String isbn) {
        return getOneMetaBuilder().withUserQuery(isbn).build();
    }

    public static MetaDto createSessionMetaDto(String searchTime) {
        return getOneMetaBuilder()
            .withSearchTime(searchTime)
            .build();
    }

    private static Builder getOneMetaBuilder() {
        return new MetaDto.Builder()
            .withTotalPages(1)
            .withTotalPages(1)
            .withCurrentPage(1)
            .withPageSize(1);
    }

    public static MetaDto createOneMetaDto(String userQuery) {
        return getOneMetaBuilder().withUserQuery(userQuery).build();
    }

    public static MetaDto createEmptyDto(String userQuery) {
        return new MetaDto.Builder()
            .withTotalElements(0)
            .withPageSize(0)
            .withTotalPages(0)
            .withUserQuery(userQuery)
            .build();
    }

}
