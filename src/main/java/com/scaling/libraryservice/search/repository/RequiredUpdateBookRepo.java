package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.batch.bookUpdate.entity.RequiredUpdateBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequiredUpdateBookRepo extends JpaRepository<RequiredUpdateBook,Long> {
    long countByNotFound(boolean notFound);
}