package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Book;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {


    //JPA 작가 검색
//    List<Book> findByAuthor(@Param("query") String query);

    //JPQL로 검색구현
//    @Query("select b from Book b where b.author like %:query%")
//    List<Book> findBooksByAuthor(@Param("query") String query);

//    @Query("select b from Book b where b.title like %:title%")
//    List<Book> findBooksByTitle(@Param("title") String title);

    // 작가검색 FULLTEXT 서치 이용
    @Query(value = "SELECT * FROM books WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    List<Book> findBooksByAuthor(@Param("query") String query);


    // 제목검색 FULLTEXT 서치 이용
    @Query(value = "SELECT * FROM books WHERE MATCH(TITLE_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    List<Book> findBooksByTitle(@Param("query") String query);

    }

