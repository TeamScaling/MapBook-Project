package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.util.recommend.RecommendRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRule recommendRule;

    private final BookRepository bookRepo;

    @Timer
    public RespRecommend getRecommendBook(RespBooksDto searchResult){
        return recommendRule.recommendBooks(searchResult);
    }

    public void getRecommendBook2(String query){

    }

}
