package com.scaling.libraryservice.search.util.relate;

import com.scaling.libraryservice.recommend.dto.RecommendBookDto;
import com.scaling.libraryservice.search.dto.RelationWords;
import java.util.List;

public interface RelationRule {

    RelationWords relatedBooks(List<RecommendBookDto> recommendBooks);
}
