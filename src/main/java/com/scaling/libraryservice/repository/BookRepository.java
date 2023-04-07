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
    @Query("select b from Book b where b.author like %:query%")
    List<Book> findByAuthor(@Param("query") String query);


}
