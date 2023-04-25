package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.mapBook.entity.Ranks;
import com.scaling.libraryservice.search.dto.QueryDto;
import com.scaling.libraryservice.search.dto.RelatedBookDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.entity.Query;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RelatedSearchRepository extends JpaRepository<Query, Long> {
    @org.springframework.data.jpa.repository.Query(value = "SELECT isbn_thirteen_no FROM ranks5000 WHERE class_no LIKE :firstDigit% ORDER BY loan_count DESC Limit 10;", nativeQuery = true)
    List<String> findRelatedQuery(@Param("firstDigit") String firstDigit);

//    @org.springframework.data.jpa.repository.Query(value = "SELECT isbn_thirteen_no FROM ranks5000 WHERE class_no LIKE :firstDigit% ORDER BY loan_count DESC Limit 30;", nativeQuery = true)
//    List<String> findRelatedQuery(@Param("firstDigit") String firstDigit);


}
