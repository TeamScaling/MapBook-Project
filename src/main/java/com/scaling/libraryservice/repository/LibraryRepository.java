package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Library;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibraryRepository extends JpaRepository<Library, Integer> {

    @Query("select l from Library l where l.twoAreaNm = :area")
    List<Library> findLibInfo(@Param("area") String area);
}
