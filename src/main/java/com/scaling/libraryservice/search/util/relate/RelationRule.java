package com.scaling.libraryservice.search.util.relate;

import com.scaling.libraryservice.search.dto.RespBooksDto;

public interface RelationRule {

    RespBooksDto relatedBooks(RespBooksDto searchResult);
}
