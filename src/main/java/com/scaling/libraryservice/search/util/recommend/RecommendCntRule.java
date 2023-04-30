package com.scaling.libraryservice.search.util.recommend;

import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.repository.BookRepository;
import org.springframework.stereotype.Component;


public class RecommendCntRule implements RecommendRule{

    private BookRepository bookRepository;

    @Override
    public RespRecommend recommendBooks(RespBooksDto respBooksDto) {



        return null;
    }
}
