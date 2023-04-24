package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // 제목검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM books WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByTitleNormal(@Param("query") String query, Pageable pageable);

    // 제목에 대한 더 넓은 검색
    @Query(value = "SELECT * FROM books WHERE MATCH(TITLE_NM) AGAINST (:query IN natural language MODE)", nativeQuery = true)
    Page<Book> findBooksByTitleDetail(@Param("query") String query, Pageable pageable);

    // 작가검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM books WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByAuthor(@Param("query") String query, Pageable pageable);

    // 제목 검색 결과가 없을 경우 재검색을 위한 쿼리
    default Page<Book> findBooksByTitleFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByTitleNormal(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByTitleDetail(query, pageable);
        }
        return books;
    }

    @Query(value = "SELECT * FROM books WHERE MATCH(ENG_TITLE_NM) AGAINST (:query IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    Page<Book> findBooksByEnglishTitleNormal(String query, Pageable pageable);
}

