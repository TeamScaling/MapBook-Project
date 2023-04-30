package com.scaling.libraryservice.recommend.util;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.recommend.dto.RespRecommend;
import com.scaling.libraryservice.search.repository.BookRepository;


public class RecommendCntRule implements RecommendRule{

    private BookRepository bookRepository;

    @Override
    public RespRecommend recommendBooks(RespBooksDto respBooksDto) {



        return null;
    }
}
