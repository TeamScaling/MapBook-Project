package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.LoanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanItemRepository extends JpaRepository<LoanItem,Integer> {

}
