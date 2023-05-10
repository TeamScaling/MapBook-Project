package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.HsAreaCd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HasBookAreaRepository extends JpaRepository<HsAreaCd,Integer> {

}
