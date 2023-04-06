package com.scaling.libraryservice.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter @Setter
public class RespBooksDto {

    private int size;
    private int page;
    private List<BookDto> documents;

    public RespBooksDto (Pageable pageable, List<BookDto> documents) {
        this.size = pageable.getPageSize();
        this.page = pageable.getPageNumber();
        this.documents = documents;
    }
}
