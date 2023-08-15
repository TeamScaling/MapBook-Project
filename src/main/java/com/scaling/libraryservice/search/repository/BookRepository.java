package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.entity.Book;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookRepository extends JpaRepository<Book,Long> {
//    Page<Book> findBooks(TitleQuery titleQuery, Pageable pageable);

    List<Book> findBookByIsbnIn(Collection<String> isbn);
}

