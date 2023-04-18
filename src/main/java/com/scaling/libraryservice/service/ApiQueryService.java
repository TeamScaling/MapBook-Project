package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.exception.OpenApiException;
import com.scaling.libraryservice.util.ParamMapCreatable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
@Setter @Getter
public class ApiQueryService {

    private final RestTemplate restTemplate;
    private String authKey = "55db267f8f05b0bf8e23e8d3f65bb67d206a6b5ce24f5e0ee4625bcf36e4e2bb";

    // OpenAPI에 단일 요청을 보낸다.
    public ResponseEntity<String> singleQuery(Map<String, String> paramMap)
        throws OpenApiException {

        Objects.requireNonNull(paramMap);

        //Param 방식의 OpenAPI에 맞춰 Uri을 동적으로 생성하는 builder
        UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromHttpUrl(paramMap.get("apiUri"))
            .queryParam("authKey", authKey);

        //매개 변수로 받은 paramMap을 순회하며, param들을 builder에 추가한다.
        paramMap.forEach((key, value) -> uriBuilder.queryParam(key, value));

        ResponseEntity<String> resp;

        try {
            resp = restTemplate.exchange(uriBuilder.toUriString(),
                HttpMethod.GET, HttpEntity.EMPTY, String.class);


        } catch (RestClientException e) {
            log.error(e.toString());

            throw e;
        }

        return resp;
    }


    // OpenApi에 대한 단일 요청 성능을 높이기 위한 멀티 쓰레드 병렬 요청
    @Timer
    @Transactional(readOnly = true)
    public List<ResponseEntity<String>> multiQuery(List<? extends ParamMapCreatable> params,
        String target,int nThreads) throws OpenApiException {

        Objects.requireNonNull(params);

        ExecutorService service = Executors.newFixedThreadPool(nThreads);

        // 멀티 쓰레드의 역할을 정의 하는 내용이 담긴 Callable list.
        List<Callable<ResponseEntity<String>>> tasks = new ArrayList<>();

        // 멀티쓰레드를 이용하여 각기 다른 요청을 보내기 위해, 요청 param을 map 형태로 만들어 반환 할 수 있는 객체 list
        for (ParamMapCreatable l : params) {

            tasks.add(() -> singleQuery(l.createParamMap(target)));
        }

        // 병렬 요청에 대한 결과를 취합하기 위한 list
        List<Future<ResponseEntity<String>>> futures;

        // 최종적으로 반환하는 데이터가 담길 list
        List<ResponseEntity<String>> result = new ArrayList<>();

        try {

            // main Thread가 모든 thread가 수행을 마칠 때까지 기다린다.
            futures = service.invokeAll(tasks);

            for (Future<ResponseEntity<String>> f : futures) {
                result.add(f.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            log.info(e.toString());

        } finally {
            service.shutdown();
        }

        return result;
    }



}
