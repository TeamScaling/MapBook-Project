package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookFinder {

    Page<BookDto> selectBooks(TitleQuery titleQuery,Pageable pageable);

    List<BookDto> selectRecommends(TitleQuery titleQuery,int size);
}
