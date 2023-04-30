package com.scaling.libraryservice.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString
public class RespBooksDto {

    private MetaDto meta;
    private List<BookDto> documents;
    private List<RecommendBookDto> recommendBooks;
    private RelationWords relationWords;

    // 기본 생성자 추가
    public RespBooksDto() {
    }

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents) {
        this.meta = metaDto;
        this.documents = documents;
    }

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents, List<RecommendBookDto> recommendBooks, RelationWords relationWords) {
        this.meta = metaDto;
        this.documents = documents;
        this.recommendBooks = recommendBooks;
        this.relationWords = relationWords;
    }

    @Override
    public String toString() {
        return " metaDto : " + meta +
            " documents :" + documents+
            " recommendBooks : " + recommendBooks +
            " relationWords : " + relationWords;
    }

    public String getTitle() {
        if (documents != null && documents.size() > 0) {
            return documents.get(0).getTitle();
        } else {
            return null;
        }
    }



}