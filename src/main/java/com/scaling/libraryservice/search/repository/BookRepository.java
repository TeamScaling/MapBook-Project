package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 제목검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByKorBool(@Param("query") String query, Pageable pageable);

    // 제목에 대한 더 넓은 검색
    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN natural language MODE)", nativeQuery = true)
    Page<Book> findBooksByKorNatural(@Param("query") String query, Pageable pageable);
    
    @Query(value = "select * from books "
        + "use index (idx_title_nm_space_based) "
        + "where match (TITLE_NM) against (:query in BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByEngBool(@Param("query") String query, Pageable pageable);

    @Query(value = "select * from books "
        + "use index (idx_title_nm_space_based) "
        + "where match (TITLE_NM) against (:query in NATURAL LANGUAGE MODE)", nativeQuery = true)
    Page<Book> findBooksByEngNatural(@Param("query") String query, Pageable pageable);

    @Timer
    @Query(value = "select * from "
        + "(select * from books "
        + "where match(TITLE_NM) against(:korToken IN BOOLEAN MODE)) "
        + "AS l where l.TITLE_NM like :engToken", nativeQuery = true,
        countQuery =
            "select count(*) from (select * from (select id_no,ISBN_THIRTEEN_NO,TITLE_NM "
                + "from books where match(TITLE_NM) against(:korToken IN BOOLEAN MODE)) as l\n"
                + "where l.TITLE_NM like :engToken) as c")
    Page<Book> findBooksByEngKorBool(@Param("engToken") String engToken,
        @Param("korToken") String korToken, Pageable pageable);


    @Query(value = "select * from "
        + "(select * from books "
        + "where match(TITLE_NM) against(:korToken IN NATURAL LANGUAGE MODE)) "
        + "AS l where l.TITLE_NM like :engToken", nativeQuery = true,
        countQuery =
            "select count(*) from (select * from (select id_no,ISBN_THIRTEEN_NO,TITLE_NM "
                + "from books where match(TITLE_NM) against(:korToken IN NATURAL LANGUAGE MODE)) as l\n"
                + "where l.TITLE_NM like :engToken) as c")
    Page<Book> findBooksByEngKorNatural(@Param("engToken") String engToken,
        @Param("korToken") String korToken, Pageable pageable);

    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN boolean mode)"
        , nativeQuery = true)
    Page<Book> findBooksByKorTitleBool(@Param("query") String query, Pageable pageable);


    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN boolean mode) order by loan_cnt desc"
        , nativeQuery = true)
    Page<Book> findBooksByKorTitleBoolOrder(@Param("query") String query, Pageable pageable);

    @Query(value = "select * from books use index (idx_title_nm_space_based) "
        + "where match (TITLE_NM) against (:query in BOOLEAN MODE) "
        + "order by loan_cnt desc ", nativeQuery = true)
    Page<Book> findBooksByEngBoolOrder(@Param("query") String query, Pageable pageable);




    // 제목 검색 결과가 없을 경우 재검색을 위한 쿼리
    default Page<Book> findBooksByKorMtFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByKorBool(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByKorNatural(query, pageable);
        }

        return books;
    }

    // 제목 검색 결과가 없을 경우 재검색을 위한 쿼리
    default Page<Book> findBooksByEngMtFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByEngBool(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByEngNatural(query, pageable);
        }
        return books;
    }

    default Page<Book> findBooksByKorSgFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByKorTitleBool(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByKorNatural(query, pageable);
        }
        return books;
    }

    default Page<Book> findBooksByEngSgFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByEngBoolOrder(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByEngNatural(query, pageable);
        }
        return books;
    }

}

