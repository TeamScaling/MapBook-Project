package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@Setter @ToString
public class RespSearchBooksDto {

    private SearchBookMetaDto meta;
    private List<BookDto> documents;

    public RespSearchBooksDto(SearchBookMetaDto searchBookMetaDto, List<BookDto> documents) {
        this.meta = searchBookMetaDto;
        this.documents = documents;
    }

    public RespSearchBooksDto(Pageable pageable, Page<Book> books, List<BookDto> documents) {
        this.documents = documents;
        this.meta = new SearchBookMetaDto();
        this.meta.setCurrentPage(pageable.getPageNumber());
        this.meta.setPageSize(pageable.getPageSize());
        this.meta.setTotalElements(books.getTotalElements());
        this.meta.setTotalPages(books.getTotalPages());
    }
}
