package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.dto.AbstractApiConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class CircuitBreaker {

    private List<AbstractApiConnection> observingConnections;

    public void receiveError(AbstractApiConnection connection){

        String apiUri = connection.getApiUrl();
        Integer errorCnt = connection.getErrorCnt();

        if(observingConnections == null){
            observingConnections = new ArrayList<>();
        }

        if(observingConnections.contains(connection)){


        }else{
            connection.upErrorCnt();
            observingConnections.add(connection);
            
        }

        log.error("error가 접수 됐습니다. 요청 api 주소 : [{}] , current error cnt : [{}]"
            ,apiUri, errorCnt);
    }

}
