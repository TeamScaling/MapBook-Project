package com.scaling.libraryservice.search.util.relate;

import com.scaling.libraryservice.search.dto.RecommendBookDto;
import com.scaling.libraryservice.search.dto.RelationWords;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import java.util.List;

public interface RelationRule {

    RelationWords relatedBooks(List<RecommendBookDto> recommendBooks);
}
