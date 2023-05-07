package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.commons.apiConnection.ApiStatus;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
public class ApiQuerySender {

    private final RestTemplate restTemplate;

    /**
     * 대상 Api에 대한 연결 상태를 확인 한다
     *
     * @param apiStatus 대상이 되는 Api에 대한 정보를 담는 객체
     * @return Api에 연결이 정상이면 true, 연결 과정에서 에러가 있으면 false
     */
    public static boolean checkConnection(ApiStatus apiStatus) {

        ResponseEntity<String> resp = null;
        try {
            resp = new RestTemplate().exchange(
                apiStatus.getApiUri(),
                HttpMethod.OPTIONS,
                HttpEntity.EMPTY,
                String.class);
        } catch (RestClientException e) {
            log.error(e.toString());
            return false;
        }

        return resp.getStatusCode().is2xxSuccessful();
    }


    public void sendPost(String jsonData, String url){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Cache data for [{}] backed up successfully",url);
        } else {
            log.error("Failed to back up cache data for [{}]", url);
        }
    }



    /**
     * 대상 Api에 요청을 보내 원하는 응답 데이터를 받는다.
     *
     * @param configUriBuilder Api에 대한 요청 param 값들을 담고 있는 객체
     * @param target           Api에 요청 하고자 하는 데이터 식별 값
     * @return Api 응답 데이터를 담는 ResponseEntity
     */
    @Timer
    public ResponseEntity<String> singleQueryJson(ConfigureUriBuilder configUriBuilder,
        String target) {

        Objects.requireNonNull(configUriBuilder);

        UriComponentsBuilder uriBuilder = configUriBuilder.configUriBuilder(target);

        uriBuilder.queryParam("format", "json");

        ResponseEntity<String> resp = null;

        try {
            resp = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);

        } catch (RestClientException e) {
            log.error(e.toString());

            if (ApiObserver.class.isAssignableFrom(configUriBuilder.getClass())) {
                ApiObserver apiObserver = (ApiObserver) configUriBuilder;

                CircuitBreaker.receiveError(apiObserver, e);
            }
        }

        return resp;
    }

    // OpenApi에 대한 단일 요청 성능을 높이기 위한 멀티 쓰레드 병렬 요청

    /**
     * Api에 대한 요청을 병렬 처리 한다.
     *
     * @param uriBuilders Api에 대한 요청 param 값들을 담고 있는 객체
     * @param target      Api에 요청 하고자 하는 데이터 식별 값
     * @param nThreads    병렬 처리를 수행할 쓰레드 갯수
     * @return Api 응답 데이터 ResponseEntity들을 담은 List
     */
    @Timer
    public List<ResponseEntity<String>> multiQuery(List<? extends ConfigureUriBuilder> uriBuilders,
        String target, int nThreads) {

        Objects.requireNonNull(uriBuilders);

        ExecutorService service = Executors.newFixedThreadPool(nThreads);

        List<Callable<ResponseEntity<String>>> tasks = new ArrayList<>();

        for (ConfigureUriBuilder b : uriBuilders) {

            tasks.add(() -> singleQueryJson(b, target));
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

        String logMeta
            = uriBuilders.get(0).configUriBuilder(target).build().getHost();

        log.info("Total API requests sent to {}: {}", logMeta, result.size());

        return result;
    }


}
