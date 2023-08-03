package com.scaling.libraryservice.dataPipe.updater.repository;

import com.scaling.libraryservice.dataPipe.updater.entity.UpdateBook;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookUpdateRepository extends JpaRepository<UpdateBook,Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT * from out_data_book where TITLE_NM is null limit :limit", nativeQuery = true)
    List<UpdateBook> findBooksWithLimit(@Param("limit") int limit);

    @Modifying @Transactional()
    @Query(value = "delete from out_data_book where id_no <= :lastId and TITLE_NM is null;",nativeQuery = true)
    void deleteNotFoundBook(@Param("lastId") long lastId);

}
