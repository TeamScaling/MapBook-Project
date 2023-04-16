package com.scaling.libraryservice.util;

import com.scaling.libraryservice.exception.OpenApiException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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
public class OpenApiQuerySender {

    private final RestTemplate restTemplate;

    public ResponseEntity<String> sendParamQuery(Map<String, String> paramMap,String apiUri)
        throws OpenApiException {

        Objects.requireNonNull(paramMap);

        UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromHttpUrl(apiUri);

        paramMap.forEach((key, value) -> uriBuilder.queryParam(key, value));

        ResponseEntity<String> resp;

        try {
            resp = restTemplate.exchange(uriBuilder.toUriString(),
                HttpMethod.GET, HttpEntity.EMPTY, String.class);

        } catch (RestClientException e) {
            log.error(e.toString());

            throw new OpenApiException(e.toString(), e);
        }

        return resp;
    }



}
