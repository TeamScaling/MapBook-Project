package com.scaling.libraryservice.search.util.recommend;

import com.scaling.libraryservice.search.dto.RespBooksDto;

public interface RecommendRule {

    RespBooksDto recommendBooks(RespBooksDto searchResult);

}
