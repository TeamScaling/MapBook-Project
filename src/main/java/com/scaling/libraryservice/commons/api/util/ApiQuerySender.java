package com.scaling.libraryservice.commons.api.util;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.domain.ApiConnection;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * ApiQuerySender 클래스는 외부 REST API에 대한 HTTP 요청을 담당합니다.
 * 요청 방식(GET, POST 등)에 따라 적절한 메소드를 선택하여 사용할 수 있습니다.
 * 특히, 병렬 처리가 필요한 경우에는 sendMultiQuery 메소드를 활용하여 여러 요청을 동시에 처리할 수 있습니다.
 * RestTemplate을 이용하여 HTTP 요청을 보내고, ResponseEntity를 통해 API 응답을 받습니다.
 */

@Slf4j
@Getter
@RequiredArgsConstructor
public class ApiQuerySender {

    private final RestTemplate restTemplate;

    public void sendPost(String jsonData, String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Cache data for [{}] backed up successfully", url);
        } else {
            log.error("Failed to back up cache data for [{}]", url);
        }
    }

    /**
     * 대상 Api에 요청을 보내 원하는 응답 데이터를 받는다.
     *
     * @param configUriBuilder Api에 대한 요청 param 값들을 담고 있는 객체
     * @return Api 응답 데이터를 담는 ResponseEntity
     * @throws OpenApiException API와의 연결에 문제가 있을 경우.
     */
    @Timer
    public ResponseEntity<String> sendSingleQuery(
        ApiConnection configUriBuilder, HttpEntity<?> httpEntity) throws OpenApiException {

        Objects.requireNonNull(configUriBuilder);

        UriComponentsBuilder uriBuilder = configUriBuilder.configUriBuilder();

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                String.class);
        } catch (Exception e) {
            throw new OpenApiException("api 문제");
        }

        return response;
    }

    /**
     * Api에 대한 요청을 병렬 처리 한다.
     *
     * @param uriBuilders Api에 대한 요청 param 값들을 담고 있는 객체
     * @param nThreads    병렬 처리를 수행할 쓰레드 갯수
     * @return Api 응답 데이터 ResponseEntity들을 담은 List
     * @throws OpenApiException API와의 연결에 문제가 있을 경우.
     */
    @Timer
    public List<ResponseEntity<String>> sendMultiQuery(
        List<? extends ApiConnection> uriBuilders, int nThreads, HttpEntity<?> httpEntity)
        throws OpenApiException {

        Objects.requireNonNull(uriBuilders);

        ExecutorService service = Executors.newFixedThreadPool(nThreads);

        List<Callable<ResponseEntity<String>>> tasks = new ArrayList<>();

        for (ApiConnection b : uriBuilders) {

            tasks.add(() -> sendSingleQuery(b, httpEntity));
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
            throw new OpenApiException("api 문제 발생");
        } finally {
            service.shutdown();
        }

        return result;
    }


}
