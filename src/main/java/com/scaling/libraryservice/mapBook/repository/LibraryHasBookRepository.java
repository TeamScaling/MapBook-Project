package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.entity.LibraryHasBook;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibraryHasBookRepository extends JpaRepository<LibraryHasBook, Long> {

    @Query("select l.library from LibraryHasBook l where l.isbn13 = :isbn13 and l.areaCd = :areaCd")
    List<Library> findHasBookLibraries(@Param("isbn13") Double isbn13,@Param("areaCd") int areaCd);

    @Query("select l.areaCd from LibraryHasBook l group by l.areaCd")
    List<Integer> findSupportedArea();

}
