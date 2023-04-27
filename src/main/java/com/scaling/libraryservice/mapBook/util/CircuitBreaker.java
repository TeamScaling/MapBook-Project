package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.mapBook.domain.ApiObservable;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CircuitBreaker {

    private final List<ApiObservable> observingConnections = new ArrayList<>();

    public void receiveError(ConfigureUriBuilder builder) throws NotSupportedException {

        if(ApiObservable.class.isAssignableFrom(builder.getClass())){

            ApiObservable observer = (ApiObservable)builder;

            String apiUri = observer.getApiUrl();
            Integer errorCnt = observer.getErrorCnt();

            if(observingConnections.contains(observer)){

                observer.upErrorCnt();

            }else{
                observer.upErrorCnt();
                observingConnections.add(observer);
            }

            log.error("error가 접수 됐습니다. 요청 api 주소 : [{}] , current error cnt : [{}]"
                ,apiUri, errorCnt);
        }else{

            throw new NotSupportedException();
        }
    }

    public static void closeObserver(ApiObservable observer){
        observer.closeAccess();
        log.info(observer+"이 서버가 닫혔어요!!!!!!!!!!!!!!!!!");
    }

}
