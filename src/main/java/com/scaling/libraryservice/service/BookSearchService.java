package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.dto.BookDto;
import com.scaling.libraryservice.dto.MetaDto;
import com.scaling.libraryservice.dto.RespBooksDto;
import com.scaling.libraryservice.entity.Book;
import com.scaling.libraryservice.repository.BookRepository;
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
    public RespBooksDto searchByAuthor(String author, int page, int size) {
        Pageable pageable = createPageable(page, size);

        String query = splitTarget(author);

        Page<Book> books = bookRepository.findBooksByAuthor(query, pageable);

        List<BookDto> document = convertToBookDtoList(books);

        MetaDto meta = createMetaDto(books, page, size);

        return new RespBooksDto(meta, document);
    }


    //제목 검색 FULLTEXT + 페이징
    @Timer
    public RespBooksDto searchByTitle(String title, int page, int size) {
        Pageable pageable = createPageable(page, size);

        String query = splitTarget(title);

        Page<Book> books = bookRepository.findBooksByTitleNormal(query, pageable);

        // 검색 결과가 없는 경우, 다른 방법으로 동적으로 검색결과 변경
        if (books.getContent().isEmpty()) {
            books = bookRepository.findBooksByTitleFlexible(query, pageable);
        }

        List<BookDto> document = convertToBookDtoList(books);

        MetaDto meta = createMetaDto(books, page, size);

        return new RespBooksDto(meta, document);
    }

    // pageable 객체에 값 전달
    public Pageable createPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    // 띄어쓰기 전처리
    private String splitTarget(String target) {
        return Arrays.stream(target.split(" "))
            .map(name -> "+" + name)
            .collect(Collectors.joining(" "));
    }

    //dto 리스트에서 참조변수에 값 전달
    private List<BookDto> convertToBookDtoList(Page<Book> books) {
        return books.getContent()
            .stream()
            .map(BookDto::new)
            .collect(Collectors.toList());
    }

    // MetaDto 에 값 넣기
    private MetaDto createMetaDto(Page<Book> books, int page, int size) {
        return new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);
    }






}





