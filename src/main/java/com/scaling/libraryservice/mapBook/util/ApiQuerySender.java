package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
@Setter @Getter
public class ApiQuerySender {

    private final RestTemplate restTemplate;

    // OpenAPI에 단일 요청을 보낸다.
    public ResponseEntity<String> singleQueryJson(UriComponentsBuilder uriBuilder)
        throws RestClientException {

        Objects.requireNonNull(uriBuilder);

        uriBuilder.queryParam("format", "json");

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
    public List<ResponseEntity<String>> multiQuery(List<? extends ConfigureUriBuilder> uriBuilders,
        String target, int nThreads) throws RestClientException {

        Objects.requireNonNull(uriBuilders);

        ExecutorService service = Executors.newFixedThreadPool(nThreads);

        List<Callable<ResponseEntity<String>>> tasks = new ArrayList<>();

        for (ConfigureUriBuilder b : uriBuilders) {

            tasks.add(() -> singleQueryJson(b.configUriBuilder(target)));
        }

        List<Future<ResponseEntity<String>>> futures;

        List<ResponseEntity<String>> result = new ArrayList<>();

        try {

            futures = service.invokeAll(tasks);

            for (Future<ResponseEntity<String>> f : futures) {
                result.add(f.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error(e.toString());

        } finally {
            service.shutdown();
        }

        return result;
    }


}