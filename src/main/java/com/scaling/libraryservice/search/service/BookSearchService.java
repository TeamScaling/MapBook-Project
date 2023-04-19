package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.SearchBookMetaDto;
import com.scaling.libraryservice.search.dto.RespSearchBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookSearchService {

    private final BookRepository bookRepository;

    //작가 검색 FULLTEXT + 페이징
    @Timer
    public RespSearchBooksDto searchByAuthor(String author, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        String query = splitTarget(author);

        Page<Book> books = bookRepository.findBooksByAuthor(query, pageable);

        List<BookDto> document = books.getContent()
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());

        SearchBookMetaDto meta =
            new SearchBookMetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespSearchBooksDto(meta, document);
    }


    //제목 검색 FULLTEXT + 페이징
    @Timer
    public RespSearchBooksDto searchByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        String query = splitTarget(title);
        Page<Book> books = bookRepository.findBooksByTitlePage(query, pageable);

        List<BookDto> documents = books.getContent()
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());

        SearchBookMetaDto meta =
            new SearchBookMetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespSearchBooksDto(meta, documents);
    }


    // 띄어쓰기 전처리
    private String splitTarget(String target) {
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));
    }

}





