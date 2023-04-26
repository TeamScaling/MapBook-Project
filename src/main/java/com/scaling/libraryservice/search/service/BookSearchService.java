package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.repository.RelatedSearchRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Timer
    public RespBooksDto searchBooks(String query, int page, int size, String target) {

        Pageable pageable = createPageable(page, size);

        Page<Book> books = findBooksByTarget(splitTarget(query), pageable, target);

        Objects.requireNonNull(books);

        List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();
//==========================작업중==============================================>

        // document 리스트에서 무작위로 10개의 요소를 추출하여 relatedBookDtos 리스트에 추가
        List<RelatedBookDto> relatedBookDtos = document.stream()
            .filter(bookDto -> bookDto.getTitle() != null)
            .map(bookDto -> new RelatedBookDto(bookDto.getRelatedTitle()))
            .distinct()
            .limit(100) // 상위 100개까지만 선택
            .collect(Collectors.toList());
        Collections.shuffle(relatedBookDtos); // 리스트를 무작위로 섞음
        relatedBookDtos = relatedBookDtos.stream()
            .limit(10) // 상위 10개만 선택
            .collect(Collectors.toList());
        System.out.println("relatedBookDtos : " + relatedBookDtos.toString());


//=========================작업끝===============================================>



        MetaDto meta
            = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document, relatedBookDtos);
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


}





