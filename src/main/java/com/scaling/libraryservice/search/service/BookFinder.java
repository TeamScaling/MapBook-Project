package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.util.TitleQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookFinder<T,V> {

    T findBooks(TitleQuery titleQuery,V target);

}
