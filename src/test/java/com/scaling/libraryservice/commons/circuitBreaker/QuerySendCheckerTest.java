package com.scaling.libraryservice.commons.circuitBreaker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class QuerySendCheckerTest {

    @InjectMocks
    private QuerySendChecker checker;

    @Mock
    private ApiQuerySender apiQuerySender;

    private final String mockUri = "http://localhost:8089/api/bookExist";
    @Mock
    private ApiObserver apiObserver;
    @Mock
    private ApiStatus apiStatus;


    @Test
    void isRestoration() {
        /* given */

        when(apiObserver.getApiStatus()).thenReturn(apiStatus);
        when(apiObserver.getApiStatus().getApiUri()).thenReturn(mockUri);
        when(apiQuerySender.sendSingleQuery(any(), any()))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));


        /* when */
        var result = checker.isRestoration(apiObserver);

        /* then */

        assertTrue(result);
    }

    @Test
    void isRestoration_error() {

        /* given */
        when(apiObserver.getApiStatus()).thenReturn(apiStatus);
        when(apiObserver.getApiStatus().getApiUri()).thenReturn(mockUri);
        Mockito.when(apiQuerySender.sendSingleQuery(any(), any()))
            .thenThrow(OpenApiException.class);

        /* when */
        var result = checker.isRestoration(apiObserver);

        /* then */

        assertFalse(result);
    }

}