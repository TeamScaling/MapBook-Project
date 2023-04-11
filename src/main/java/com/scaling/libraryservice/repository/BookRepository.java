package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Book;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // 작가검색 FULLTEXT 서치 이용
    @Query(value = "SELECT * FROM test.books WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    List<Book> findBooksByAuthor(@Param("query") String query);

    // 제목검색 FULLTEXT 서치 이용
    @Query(value = "SELECT * FROM test.books WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    List<Book> findBooksByTitle(@Param("query") String query);

    // 작가검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM test.books WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)",
        countQuery = "SELECT COUNT(*) FROM test.books WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)",
        nativeQuery = true)
    Page<Book> findBooksByAuthorPage(@Param("query") String query, Pageable pageable);

    // 제목검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM test.books WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByTitlePage(@Param("query") String query, Pageable pageable);

    }

