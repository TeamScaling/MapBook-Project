package com.scaling.libraryservice.search.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MetaDto {

    private long totalPages;
    private long totalElements;
    private long currentPage;
    private long pageSize;

    public MetaDto(long totalPages, long totalElements, long currentPage, long pageSize) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

}