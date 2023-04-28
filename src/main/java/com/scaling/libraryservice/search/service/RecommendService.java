package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespRecommend;
import com.scaling.libraryservice.search.util.recommend.RecommendRule;
import org.springframework.stereotype.Component;

@Component
public class RecommendService {

    private final RecommendRule recommendRule;

    public RecommendService(RecommendRule recommendRule) {
        this.recommendRule = recommendRule;
    }

    @Timer
    public RespRecommend getRecommendBook(RespBooksDto searchResult){
        return recommendRule.recommendBooks(searchResult);
    }

    public void getRecommendBook2(String query){

    }

}
