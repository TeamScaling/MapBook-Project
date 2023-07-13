package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.util.TitleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SelectStrategy {

    Page<Book> select(TitleQuery titleQuery, Pageable pageable);

}
