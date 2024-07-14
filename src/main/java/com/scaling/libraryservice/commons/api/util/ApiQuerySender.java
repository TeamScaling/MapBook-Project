package com.scaling.libraryservice.commons.api.util;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.commons.timer.MeasureTaskTime;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

/**
 * ApiQuerySender 클래스는 외부 REST API에 대한 HTTP 요청을 담당합니다. 요청 방식(GET, POST 등)에 따라 적절한 메소드를 선택하여 사용할 수
 * 있습니다. 특히, 병렬 처리가 필요한 경우에는 sendMultiQuery 메소드를 활용하여 여러 요청을 동시에 처리할 수 있습니다. RestTemplate을 이용하여 HTTP
 * 요청을 보내고, ResponseEntity를 통해 API 응답을 받습니다.
 */

@Slf4j
@RequiredArgsConstructor
public class ApiQuerySender {

    private final RestTemplate restTemplate;

    /**
     * 대상 Api에 요청을 보내 원하는 응답 데이터를 받는다.
     *
     * @param apiConnection Api에 대한 요청 param 값들을 담고 있는 객체
     * @return Api 응답 데이터를 담는 ResponseEntity
     * @throws OpenApiException API와의 연결에 문제가 있을 경우.
     */
    @MeasureTaskTime
    public ResponseEntity<String> sendSingleQuery(ApiConnection apiConnection,
        HttpEntity<?> httpEntity) throws OpenApiException {

        try {
            return restTemplate.exchange(apiConnection.configUriBuilder().toUriString(),
                HttpMethod.GET,
                httpEntity,
                String.class
            );
        } catch (Exception e) {
            throw new OpenApiException("apiObserver 문제");
        }
    }

    /**
     * Api에 대한 요청을 병렬 처리 한다.
     *
     * @param apiConnections Api에 대한 요청 param 값들을 담고 있는 객체
     * @param nThreads       병렬 처리를 수행할 쓰레드 갯수
     * @return Api 응답 데이터 ResponseEntity들을 담은 List
     * @throws OpenApiException API와의 연결에 문제가 있을 경우.
     */
    @MeasureTaskTime
    public List<ResponseEntity<String>> sendMultiQuery(List<? extends ApiConnection> apiConnections,
        int nThreads, HttpEntity<?> httpEntity) throws OpenApiException {
        Objects.requireNonNull(apiConnections);

        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        List<CompletableFuture<ResponseEntity<String>>> futures = apiConnections.stream()
            .map(conn ->
                CompletableFuture.supplyAsync(() -> sendSingleQuery(conn, httpEntity), service))
            .toList();

        try {
            return futures.stream()
                .map(CompletableFuture::join)
                .toList();
        } catch (CompletionException e) {
            log.error(e.toString());
            throw new OpenApiException("apiObserver 문제 발생");
        } finally {
            service.shutdown();
        }
    }


}
