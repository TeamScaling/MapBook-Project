package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SearchBookMetaDto {

    private long totalPages;
    private long totalElements;
    private long currentPage;
    private long pageSize;

    public SearchBookMetaDto(long totalPages, long totalElements, long currentPage, long pageSize) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

}
