package com.scaling.libraryservice.commons.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.api.service.provider.LoanableLibProvider;
import com.scaling.libraryservice.commons.api.util.binding.BindingStrategy;
import com.scaling.libraryservice.commons.api.util.binding.BindingStrategyFactory;
import com.scaling.libraryservice.commons.api.util.binding.BindingStrategyFactoryBean;
import com.scaling.libraryservice.commons.api.util.binding.LoanableLibBinding;
import com.scaling.libraryservice.mapBook.dto.ApiLoanableLibDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ApiQueryBinderTest {
    private ApiQueryBinder<ApiLoanableLibDto> apiQueryBinder;
    @Mock
    private ApiQuerySender apiQuerySender;
    @Mock
    private ApiConnection apiConnection;

    @Mock
    private BindingStrategyFactory bindingStrategyFactory;

    @BeforeEach
    public void setUp(){

        this.apiQueryBinder = new ApiQueryBinder<>(bindingStrategyFactory);
    }


    @Test
    public void test_bind() {
        /* given */

        String targetIsbn = "9788965700036";

        String body = "{\n"
            + "    \"response\": {\n"
            + "        \"request\": {\n"
            + "            \"isbn13\": \"9788965700036\",\n"
            + "            \"libCode\": \"141099\"\n"
            + "        },\n"
            + "        \"result\": {\n"
            + "            \"hasBook\": \"Y\",\n"
            + "            \"loanAvailable\": \"NN_TOKEN\"\n"
            + "        }\n"
            + "    }\n"
            + "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(body,HttpStatus.OK);
        BindingStrategy loanableLibBinding = new LoanableLibBinding();
        /* when */

        when(bindingStrategyFactory.getBindingStrategy(LoanableLibProvider.class))
            .thenReturn(loanableLibBinding);

        when(apiQuerySender.sendSingleQuery(apiConnection, HttpEntity.EMPTY))
            .thenReturn(responseEntity);

        var response = apiQuerySender.sendSingleQuery(apiConnection,HttpEntity.EMPTY);

        var result = apiQueryBinder.bind(response, LoanableLibProvider.class);

        /* then */

        assertEquals(result.getIsbn13(),targetIsbn);
    }

}