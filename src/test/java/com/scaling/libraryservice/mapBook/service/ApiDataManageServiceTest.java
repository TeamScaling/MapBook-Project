package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.mapBook.util.ApiQueryBinder;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiDataManageServiceTest {

    @Autowired
    private ApiDataManageService apiDataManageService;

    @Autowired
    private ApiQuerySender apiQuerySender;

    @Autowired
    private ApiQueryBinder apiQueryBinder;

    /*@Test
    public void save_item(){
        *//* given *//*

        var json = apiQuerySender.singleQuery(new LoanItemDto().configUriBuilder("5000"));


        var result = apiQueryBinder.bindLoanItem(json);


        apiDataManageService.addLoanItemList(result);
    }*/

}