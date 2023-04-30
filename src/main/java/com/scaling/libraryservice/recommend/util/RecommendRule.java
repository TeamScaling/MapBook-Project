package com.scaling.libraryservice.recommend.util;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.recommend.dto.RespRecommend;

public interface RecommendRule {

    RespRecommend recommendBooks(RespBooksDto searchResult);

}
