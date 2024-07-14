package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword,Long> {
}
