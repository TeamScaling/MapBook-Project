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
    /*consider : books 테이블에서 author 부분 인덱스 설정해서, 검색 속도 빨라지는지 봅시다. 근데
            아마도 "남궁성,홍길동 저" 식이라서 좀 변환은 필요 할 것입니다. 인덱스 설정해서 검색할꺼면 like문은 아마 쓰면 안될꺼에요.*/
//    @Query("select b from Book b where b.author like %:query%")
//    List<Book> findBooksByAuthor(@Param("query") String query);

    @Query("select b from Book b where b.title like %:title%")
    List<Book> findBooksByTitle(@Param("title") String title);

    //FULLTEXT 서치 이용
    @Query(value = "SELECT * FROM test.books WHERE MATCH(AUTHR_NM) AGAINST (:query IN BOOLEAN MODE)", nativeQuery = true)
    List<Book> findBooksByAuthor(@Param("query") String query);


}
