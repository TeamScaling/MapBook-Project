package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{

//    @Query(value = "select * from bookinfo.book where TITLE_NM like '%title_nm%'", nativeQuery = true)
//    List<Book> findBookByTitle_nm(@Param("title_nm") String title_nm);


//     JPQL 쿼리문
//    @Query("SELECT b FROM Book b WHERE b.title_nm LIKE %:title_nm%")
//    List<Book> findByTitle(@Param("title_nm") String title_nm);

//     SQL 쿼리문
    @Query(value = "SELECT * FROM bookinfo.book WHERE TITLE_NM LIKE %:title_nm%", nativeQuery = true)
    List<Book> findBooksByTitle_nm(@Param("title_nm") String title_nm);

/*    @Query(value = "select * from bookinfo.book where TITLE_NM like '%title_nm%'", nativeQuery = true)
    Book findBookByTitle_nm(@Param("title_nm") String title_nm);*/

//    @Query(value = "select * from bookinfo.book where TITLE_NM = :title_nm", nativeQuery = true)
//    Book findBookByTitle_nm(@Param("title_nm") String title_nm);

//    @Query(value = "SELECT * FROM bookinfo.book WHERE id = :bookId", nativeQuery = true)
//    Book findBookById(@Param("bookId") Long bookId);
}
