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

    // 도서 검색
    public RespBooksDto searchBooksFlexible(String query, int page, int size, String target) {

        Pageable pageable = createPageable(page, size);

        Page<Book> books = findBooksByTarget(splitTarget(query), pageable, target);

        List<BookDto> document = convertToBookDtoList(books);

        MetaDto meta = createMetaDto(books, page, size);

        return new RespBooksDto(meta, document);
    }


    // pageable 객체에 값 전달
    public Pageable createPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    // target에 따라 쿼리 선택하여 동적으로 변동
    private Page<Book> findBooksByTarget(String query, Pageable pageable, String target) {
        if (target.equals("author")) {
            return bookRepository.findBooksByAuthor(query, pageable);
        } else if (target.equals("title")) {
            return bookRepository.findBooksByTitleFlexible(query, pageable);
        } else {
            return null; //api 추가될 것 고려하여 일단 Null로 넣어놓음
        }
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





