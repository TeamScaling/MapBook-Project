package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.repository.RelatedSearchRepository;
import java.util.Arrays;
import java.util.List;
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

    private final RelatedSearch relatedSearch;

    // 도서 검색
    public RespBooksDto searchBooks(String query, int page, int size, String target) {

        Pageable pageable = createPageable(page, size);

        Page<Book> books = findBooksByTarget(splitTarget(query), pageable, target);

        Objects.requireNonNull(books);

        List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();
//==========================작업중==============================================>
        //relatedSearch레포에서 번호로 검색
        List<String> relatedRanks = relatedSearch.findRelatedRanks(document);
        System.out.println("relatedRanks : " + relatedRanks.toString());

//         ISBN 번호로 책 목록 조회

        List<Book> relatedBooks = bookRepository.findBooksByIsbnList(relatedRanks);
        List<BookDto> relatedDocument = relatedBooks.stream()
            .map(book -> {
                BookDto bookDto = new BookDto(book);
                bookDto.setTitle(book.getRelatedTitle());
                return bookDto;
            })
            .collect(Collectors.toList());


        System.out.println("relatedDocument : "+relatedDocument);

        List<RelatedBookDto> relatedBookDtos = relatedDocument.stream()
            .map(bookDto -> new RelatedBookDto(bookDto.getTitle()))
            .distinct()
            .collect(Collectors.toList());
        System.out.println("relatedBookDtos : " + relatedBookDtos.toString());


//=========================작업끝===============================================>



        MetaDto meta
            = new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

        return new RespBooksDto(meta, document,relatedBookDtos);
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





