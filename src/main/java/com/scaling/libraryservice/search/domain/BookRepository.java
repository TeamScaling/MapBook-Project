package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{


//     JPQL 쿼리문
//    @Query("SELECT b FROM Book b WHERE b.title_nm LIKE %:title_nm%")
//    List<Book> findByTitle(@Param("title_nm") String title_nm);

//     SQL 쿼리문
    @Query(value = "SELECT * FROM bookinfo.books WHERE TITLE_NM LIKE %:title_nm%", nativeQuery = true)
    List<Book> findBooksByTitle_nm(@Param("title_nm") String title_nm);//

}
