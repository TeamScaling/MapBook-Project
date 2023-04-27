package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // fixme : 메소드 이름 변경  findBooksByTitleNormal
    // 제목검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByKorBool(@Param("query") String query, Pageable pageable);

    // fixme : 메소드 이름 변경  findBooksByTitleDetail
    // 제목에 대한 더 넓은 검색
    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN natural language MODE)", nativeQuery = true)
    Page<Book> findBooksByKorNatural(@Param("query") String query, Pageable pageable);

    // 작가검색 FULLTEXT 서치 이용 + 페이징
    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    Page<Book> findBooksByAuthor(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM books WHERE MATCH(ENG_TITLE_NM) AGAINST (:query IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    Page<Book> findBooksByEnglishTitleNormal(String query, Pageable pageable);


    // spring boot 입력시 검색 가능 / springBoot는 검색 안됨.
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
            "select count(*) from (select * from (select SEQ_NO,ISBN_THIRTEEN_NO,TITLE_NM "
                + "from books where match(TITLE_NM) against(:korToken IN BOOLEAN MODE)) as l\n"
                + "where l.TITLE_NM like :engToken) as c")
    Page<Book> findBooksByEngKorBool(@Param("engToken") String engToken,
        @Param("korToken") String korToken, Pageable pageable);


    @Query(value = "select * from "
        + "(select * from books "
        + "where match(TITLE_NM) against(:korToken IN NATURAL LANGUAGE MODE)) "
        + "AS l where l.TITLE_NM like :engToken", nativeQuery = true,
        countQuery =
            "select count(*) from (select * from (select SEQ_NO,ISBN_THIRTEEN_NO,TITLE_NM "
                + "from books where match(TITLE_NM) against(:korToken IN NATURAL LANGUAGE MODE)) as l\n"
                + "where l.TITLE_NM like :engToken) as c")
    Page<Book> findBooksByEngKorNatural(@Param("engToken") String engToken,
        @Param("korToken") String korToken, Pageable pageable);

    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN natural language MODE) "
        + "order by loan_cnt"
        , nativeQuery = true)
    Page<Book> findBooksByKorTitleNaturalOrder(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN boolean mode) order by loan_cnt desc"
        , nativeQuery = true)
    Page<Book> findBooksByKorTitleBoolOrder(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN boolean mode)"
        , nativeQuery = true)
    Page<Book> findBooksByKorTitleBool(@Param("query") String query, Pageable pageable);

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

    default Page<Book> findBooksBySgKorFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByKorTitleBool(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByKorNatural(query, pageable);
        }
        return books;
    }

    default Page<Book> findBooksBySgEngFlexible(String query, Pageable pageable) {
        Page<Book> books = findBooksByEngBoolOrder(query, pageable);
        if (books.getContent().isEmpty()) {
            books = findBooksByEngNatural(query, pageable);
        }
        return books;
    }

}

