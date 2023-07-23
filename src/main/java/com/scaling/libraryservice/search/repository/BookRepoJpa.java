package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepoJpa extends JpaRepository<Book, Page<Book>> {

    @Query(value = "select *\n"
        + "from (select *\n"
        + "      from book_final\n"
        + "      where match(title_token) against(:nnWord in boolean MODE)\n"
        + "      ) as s\n"
        + "where match(title_nm) against(:etcWord in natural LANGUAGE MODE)", nativeQuery = true,
        countQuery = "select count(*)\n"
            + "from (select *\n"
            + "      from book_final\n"
            + "      where match(title_token) against(:nnWord in boolean MODE)\n"
            + "      ) as s\n"
            + "where match(title_nm) against(:etcWord in natural LANGUAGE MODE)")
    Page<Book> searchBookComplex(@Param("nnWord") String nnWord, @Param("etcWord") String etcWord,Pageable pageable);

}
