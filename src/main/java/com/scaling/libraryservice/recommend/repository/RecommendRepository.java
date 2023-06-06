package com.scaling.libraryservice.recommend.repository;

import com.scaling.libraryservice.search.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN natural language MODE)"
        + "order by loan_cnt desc limit :size"
        , nativeQuery = true)
    List<Book> findBooksByKorNaturalOrder(@Param("query") String query,
        @Param("size") Integer size);
    @Query(value = "select * from books "
        + "use index (idx_title_nm_space_based) "
        + "where match (TITLE_NM) against (:query in BOOLEAN MODE)"
        + "order by loan_cnt desc limit :size"
        , nativeQuery = true)
    List<Book> findBooksByEngBoolOrder(@Param("query") String query, @Param("size") Integer size);

    @Query(value = "select * from "
        + "(select * from books "
        + "where match(TITLE_NM) against(:korToken IN BOOLEAN MODE))"
        + "AS l where l.TITLE_NM like :engToken order by loan_cnt desc limit :size",
        nativeQuery = true)
    List<Book> findBooksByEngKorBoolOrder(@Param("engToken") String engToken,
        @Param("korToken") String korToken, @Param("size") Integer size);

    @Query(value = "select * from "
        + "(select * from books "
        + "where match(TITLE_NM) against(:korToken IN NATURAL LANGUAGE MODE)) "
        + "AS l where l.TITLE_NM like :engToken "
        + "order by loan_cnt desc limit :size"
        , nativeQuery = true)
    List<Book> findBooksByEngKorNaturalOrder(@Param("engToken") String engToken,
        @Param("korToken") String korToken, @Param("size") Integer size);

    @Query(value = "SELECT * FROM books "
        + "WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE) "
        + "order by loan_cnt desc limit :size"
        , nativeQuery = true)
    List<Book> findBooksByKorBoolOrder(@Param("query") String query, @Param("size") Integer size);

    @Query(value = "select * from books "
        + "use index (idx_title_nm_space_based) "
        + "where match (TITLE_NM) against (:query in NATURAL LANGUAGE MODE)"
        + "order by loan_cnt desc limit :size",
        nativeQuery = true)
    List<Book> findBooksByEngNaturalOrder(@Param("query") String query,
        @Param("size") Integer size);


    default List<Book> findBooksByKorMtFlexible(String query, Integer size) {

        List<Book> books = findBooksByKorBoolOrder(query, size);
        if (books.isEmpty()) {
            books = findBooksByKorNaturalOrder(query, size);
        }

        return books;
    }

    default List<Book> findBooksByEngMtOrderFlexible(String query, Integer size) {

        List<Book> books = findBooksByEngBoolOrder(query, size);
        if (books.isEmpty()) {
            books = findBooksByEngNaturalOrder(query, size);
        }
        return books;
    }

}
