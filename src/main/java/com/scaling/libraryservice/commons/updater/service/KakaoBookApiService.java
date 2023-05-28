package com.scaling.libraryservice.commons.updater.service;

import com.scaling.libraryservice.commons.api.util.ApiQueryBinder;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.commons.updater.dto.BookApiDto;
import com.scaling.libraryservice.mapBook.domain.ApiConnection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
public class KakaoBookApiService {

    private final ApiQuerySender apiQuerySender;

    private final ApiQueryBinder<BookApiDto> apiQueryBinder;

    @Timer
    public List<BookApiDto> getBookMulti(List<? extends ApiConnection> conns,
        int nThreads) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK e3354f2e73c173cb2d0420123c89c961");

        List<ResponseEntity<String>> responseEntities = apiQuerySender.sendMultiQuery(conns,
            nThreads, new HttpEntity<>("", headers));

        return responseEntities.stream().map(apiQueryBinder::bind).toList();
    }

}
