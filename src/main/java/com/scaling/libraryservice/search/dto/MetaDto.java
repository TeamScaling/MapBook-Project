package com.scaling.libraryservice.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

@Setter
@Getter
@NoArgsConstructor
@ToString @Builder
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

    public MetaDto(Page<BookDto> books, ReqBookDto reqBookDto) {
        this.totalPages = books.getTotalPages();
        this.totalElements = books.getTotalElements();
        this.currentPage = reqBookDto.getPage();
        this.pageSize = reqBookDto.getSize();
    }

    public MetaDto(JSONObject jsonObject) {
        this.totalPages = jsonObject.getLong("totalPages");
        this.totalElements = jsonObject.getLong("totalElements");
        this.currentPage = jsonObject.getLong("currentPage");
        this.pageSize = jsonObject.getLong("pageSize");
    }
}