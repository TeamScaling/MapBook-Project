package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.dto.LoanItemDto;
import com.scaling.libraryservice.mapBook.service.ApiBindService;
import com.scaling.libraryservice.mapBook.service.ApiDataManageService;
import com.scaling.libraryservice.mapBook.service.ApiQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private ApiDataManageService updateService;

    private ApiQueryService apiQueryService;

    private ApiBindService apiBindService;

    @BeforeEach
    public void setUp() {
        apiQueryService = new ApiQueryService(new RestTemplate());
        apiBindService = new ApiBindService();
    }

    @Test
    void contextLoads() {

        var result
            = apiQueryService.singleQuery(LoanItemDto.createParamMap(5000));

        var list= apiBindService.getLoanItem(result);

        updateService.addLoanItemList(list);

    }

}
