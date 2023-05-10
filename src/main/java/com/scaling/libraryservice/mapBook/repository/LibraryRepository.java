package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.Library;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer> {

    List<Library> findAllByAreaCd(Integer areaCd);

}
