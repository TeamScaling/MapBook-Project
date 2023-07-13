package com.scaling.libraryservice.search.service.strategy;

import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.TitleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KorBoolSt implements SelectStrategy {

    private final BookRepository bookRepository;

    @Override
    public Page<Book> select(TitleQuery titleQuery, Pageable pageable) {
        return bookRepository.findBooksByKorMtFlexible(titleQuery.getKorToken(), pageable);
    }
}
