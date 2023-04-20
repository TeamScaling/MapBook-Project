package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import com.scaling.libraryservice.mapBook.util.ApiQueryBinder;
import com.scaling.libraryservice.mapBook.service.ApiDataManageService;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private ApiDataManageService updateService;

    private ApiQuerySender apiQuerySender;

    private ApiQueryBinder apiQueryBinder;

    @BeforeEach
    public void setUp() {
        apiQuerySender = new ApiQuerySender(new RestTemplate());
        apiQueryBinder = new ApiQueryBinder();
    }

    @Test
    void contextLoads() {

        var result
            = apiQuerySender.singleQueryJson(new LoanItemDto().configUriBuilder("100"));

        var list= apiQueryBinder.bindLoanItem(result);

        updateService.addLoanItemList(list);

    }

}
