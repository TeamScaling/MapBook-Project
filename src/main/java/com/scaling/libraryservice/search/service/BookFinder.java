package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookFinder<T> {

    Page<T> findBooks(TitleQuery titleQuery,Pageable pageable);

    List<T> findRecommends(TitleQuery titleQuery,int size);
}
