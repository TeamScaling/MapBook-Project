package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Library;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibraryRepository extends JpaRepository<Library, Integer> {

    @Query("select l from Library l where l.libArea = :area")
    List<Library> findLibInfo(@Param("area") String area);

    @Query(value = "SELECT *, "
        + "(6371 * acos( cos( radians(:lat) ) * cos( radians( LBRRY_LA ) ) * cos( radians( LBRRY_LO ) - radians(:lon) ) + sin( radians(:lat) ) * sin( radians( LBRRY_LA ) ) )) AS distance "
        + "FROM lib_info HAVING distance < 5 ORDER BY distance LIMIT 20;", nativeQuery = true)
    List<Library> findNearestLibrary(@Param("lat") double lat, @Param("lon") double lon);

}
