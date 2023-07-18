package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.util.TitleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookRepository{
    Page<BookDto> findBooks(TitleQuery titleQuery, Pageable pageable);
}

