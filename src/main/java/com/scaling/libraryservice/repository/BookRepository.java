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

    //가장 기본으로 단순히 책 제목이 포함된 도서 데이터 찾기.
//    @Query("select b from Book b where b.title like %:title% ")
//    List<Book> findBooksByTitle(@Param("query") String title);
//
//    @Query("select b from Book b where b.title like %:title% and b.content like %:title%")
//    List<Book> findBooksByTitleAndContent(@Param("query") String title);

//    @Query("select b from Book b where b.author like %:query%")
//    List<Book> findBooksByAuthor(@Param("query") String query);

    List<Book> findByAuthor(@Param("query") String query);


}
