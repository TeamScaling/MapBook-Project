package com.scaling.libraryservice.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;


@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class MetaDto {

    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private String searchTime;

    private String userQuery;

    public void addSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public MetaDto(@NonNull Page<BookDto> books, @NonNull ReqBookDto reqBookDto) {
        this.totalPages = books.getTotalPages();
        this.totalElements = books.getTotalElements();
        this.currentPage = reqBookDto.getPage();
        this.pageSize = reqBookDto.getSize();
        this.userQuery = reqBookDto.getUserQuery();
    }

    public void changeQueryToUserQuery(String userQuery) {
        this.userQuery = userQuery;
    }

    public MetaDto(String searchTime) {
        this.searchTime = searchTime;
    }


    public static class Builder {

        private int totalPages;
        private long totalElements;
        private int currentPage;
        private int pageSize;
        private String searchTime;
        private String userQuery;

        public Builder withTotalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder withTotalElements(int totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder withCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder withPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder withSearchTime(String searchTime) {
            this.searchTime = searchTime;
            return this;
        }

        public Builder withUserQuery(String userQuery) {
            this.userQuery = userQuery;
            return this;
        }

        public MetaDto build() {
            MetaDto metaDto = new MetaDto();
            metaDto.totalPages = this.totalPages;
            metaDto.totalElements = this.totalElements;
            metaDto.currentPage = this.currentPage;
            metaDto.pageSize = this.pageSize;
            metaDto.searchTime = this.searchTime;
            metaDto.userQuery = this.userQuery;

            return metaDto;
        }
    }



}