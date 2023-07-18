package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepoJPA extends JpaRepository<Book, Page<Book>> {

}
