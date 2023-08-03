package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.LibraryInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LibraryRepository extends JpaRepository<LibraryInfo, Integer> {

    @Transactional(readOnly = true)
    List<LibraryInfo> findAllByAreaCd(Integer areaCd);

}
