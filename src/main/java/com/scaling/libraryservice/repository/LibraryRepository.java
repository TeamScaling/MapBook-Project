package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library,Integer> {

}
