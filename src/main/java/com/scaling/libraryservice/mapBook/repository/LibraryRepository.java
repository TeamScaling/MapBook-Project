package com.scaling.libraryservice.mapBook.repository;

import com.scaling.libraryservice.mapBook.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer> {

}
