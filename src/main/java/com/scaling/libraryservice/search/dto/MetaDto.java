package com.scaling.libraryservice.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MetaDto {

    private String query;
    private long totalPages;
    private long totalElements;
    private long currentPage;
    private long pageSize;

    private String searchTime;

    public MetaDto(long totalPages, long totalElements, long currentPage, long pageSize,String searchTime) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.searchTime = searchTime;
    }

    public MetaDto(@NonNull Page<BookDto> books,@NonNull ReqBookDto reqBookDto,String searchTime) {
        this.query = reqBookDto.getQuery();
        this.totalPages = books.getTotalPages();
        this.totalElements = books.getTotalElements();
        this.currentPage = reqBookDto.getPage();
        this.pageSize = reqBookDto.getSize();
        this.searchTime = searchTime;
    }

    public MetaDto(@NonNull Page<BookDto> books,@NonNull ReqBookDto reqBookDto) {
        this.totalPages = books.getTotalPages();
        this.totalElements = books.getTotalElements();
        this.currentPage = reqBookDto.getPage();
        this.pageSize = reqBookDto.getSize();
    }

    public MetaDto(@NonNull JSONObject jsonObject) {
        this.totalPages = jsonObject.getLong("totalPages");
        this.totalElements = jsonObject.getLong("totalElements");
        this.currentPage = jsonObject.getLong("currentPage");
        this.pageSize = jsonObject.getLong("pageSize");
    }
}