package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.dto.BookDto;
import com.scaling.libraryservice.dto.MetaDto;
import com.scaling.libraryservice.dto.RespBooksDto;
import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.repository.BookQueryRepository;
import com.scaling.libraryservice.repository.BookRepository;
import com.scaling.libraryservice.util.Tokenizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final BookQueryRepository bookQueryRepo;
    private final Tokenizer tokenizer;
    private final BookRepository bookRepository;

    // 기존 검색 + 페이징
    public Page<RespBooksDto> searchBookPage(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<String> tokens = tokenizer.tokenize(title);

        Page<Book> books = bookQueryRepo.findBooksByToken(tokens, pageable);

        List<BookDto> documents = books.getContent()
            .stream().map(BookDto::new).toList();

        RespBooksDto respBooksDto = new RespBooksDto(pageable, books, documents);

        return new PageImpl<>(Arrays.asList(respBooksDto), pageable,
            books.getTotalElements());
    }

    public RespBooksDto searchBook(String title) {
        List<String> token = tokenizer.tokenize(title);

        List<BookDto> books = bookQueryRepo.findBooksByToken(token)
            .stream().map(BookDto::new).toList();

        return new RespBooksDto(new MetaDto(), books);
    }


    //작가 검색 FULLTEXT
    @Timer
    public RespBooksDto searchByAuthor(String author) {

        String query = splitTarget(author);

        List<BookDto> books = bookRepository.findBooksByAuthor(query)
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());

        return new RespBooksDto(new MetaDto(), books);
    }

    //작가 검색 FULLTEXT + 페이징
    @Timer
    public RespBooksDto searchByAuthor(String author, int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);

        String query = splitTarget(author);

        Page<Book> books = bookRepository.findBooksByAuthorPage(query, pageable);

        List<BookDto> document = books.getContent()
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());

        MetaDto meta = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);
        return new RespBooksDto(meta, document);
    }


    //제목 검색 FULLTEXT
    @Timer
    public RespBooksDto searchByTitle(String title) {

        String query = splitTarget(title);

        List<BookDto> books = bookRepository.findBooksByTitle(query)
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());

        return new RespBooksDto(new MetaDto(), books);
    }

    //제목 검색 FULLTEXT + 페이징
    @Timer
    public RespBooksDto searchByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);

        String query = splitTarget(title);
        Page<Book> books = bookRepository.findBooksByTitlePage(query, pageable);

        List<BookDto> document = books.getContent()
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());

        MetaDto meta = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);
        return new RespBooksDto(meta, document);
    }


    // 띄어쓰기 전처리
    private String splitTarget(String target) {
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name )
            .collect(Collectors.joining(" "));
    }



}





