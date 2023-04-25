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
public class RespBooksDto {

    private MetaDto meta;
    private List<BookDto> documents;

    private List<RelatedBookDto> relatedBookDtoList;

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents, List<RelatedBookDto> relatedBookDtos) {
        this.meta = metaDto;
        this.documents = documents;
        this.relatedBookDtoList = relatedBookDtos;
    }

    public RespBooksDto(Pageable pageable, Page<Book> books, List<BookDto> documents) {
        this.documents = documents;
        this.meta = new MetaDto();
        this.meta.setCurrentPage(pageable.getPageNumber());
        this.meta.setPageSize(pageable.getPageSize());
        this.meta.setTotalElements(books.getTotalElements());
        this.meta.setTotalPages(books.getTotalPages());
    }
}