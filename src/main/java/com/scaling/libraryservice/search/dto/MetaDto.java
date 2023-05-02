package com.scaling.libraryservice.search.dto;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

@Setter
@Getter
@NoArgsConstructor
@ToString
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

    public MetaDto(JSONObject jsonObject) {
        this.totalPages = jsonObject.getLong("totalPages");
        this.totalElements = jsonObject.getLong("totalElements");
        this.currentPage = jsonObject.getLong("currentPage");
        this.pageSize = jsonObject.getLong("pageSize");

    }
}