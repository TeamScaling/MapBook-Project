package com.scaling.libraryservice.search.util.recommend;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;

public interface RecommendRule {

    RespRecommend recommendBooks(RespBooksDto searchResult);

}
