package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.LibraryInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<LibraryInfo, Integer> {

    List<LibraryInfo> findAllByAreaCd(Integer areaCd);

}
