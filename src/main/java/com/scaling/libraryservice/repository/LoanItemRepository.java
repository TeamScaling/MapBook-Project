package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.LoanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanItemRepository extends JpaRepository<LoanItem,Integer> {

}
