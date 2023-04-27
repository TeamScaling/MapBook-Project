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
    private List<RelatedBookDto> relatedBooks;
    private TokenDto tokenDto;

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents) {
        this.meta = metaDto;
        this.documents = documents;
    }

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents, List<RelatedBookDto> relatedBooks, TokenDto tokenDto) {
        this.meta = metaDto;
        this.documents = documents;
        this.relatedBooks = relatedBooks;
        this.tokenDto = tokenDto;
    }

    public RespBooksDto(Pageable pageable, Page<Book> books, List<BookDto> documents) {
        this.documents = documents;
        this.meta = new MetaDto();
        this.meta.setCurrentPage(pageable.getPageNumber());
        this.meta.setPageSize(pageable.getPageSize());
        this.meta.setTotalElements(books.getTotalElements());
        this.meta.setTotalPages(books.getTotalPages());
    }

    public void setRelatedBooks(List<RelatedBookDto> relatedBooks) {
        this.relatedBooks = relatedBooks;
    }

    public void setTokenDto(TokenDto tokenDto) {
        this.tokenDto = tokenDto;
    }
}