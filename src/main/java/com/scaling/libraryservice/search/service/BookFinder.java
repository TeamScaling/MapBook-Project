package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.domain.TitleQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookFinder<T> {

    Page<T> selectBooks(TitleQuery titleQuery,Pageable pageable);

    List<T> selectRecommends(TitleQuery titleQuery,int size);
}
