package com.scaling.libraryservice.commons.api.util;

import com.scaling.libraryservice.commons.api.service.provider.DataProvider;
import com.scaling.libraryservice.commons.api.service.provider.LoanableLibProvider;
import com.scaling.libraryservice.commons.api.service.provider.KakaoBookProvider;
import com.scaling.libraryservice.commons.api.util.binding.BindingStrategy;
import com.scaling.libraryservice.commons.api.util.binding.LoanableLibBinding;
import com.scaling.libraryservice.commons.api.util.binding.KakaoBookBinding;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * API의 응답 결과를 객체로 Binding 하는 클래스.
 *
 * @param <T> Binding하고자 하는 API Connection 객체의 DTO 타입
 */
@Slf4j
@Component
public class ApiQueryBinder<T> {

    private final Map<Class<?>, BindingStrategy<?>> bindingStrategyMap;

    public ApiQueryBinder() {
        this.bindingStrategyMap = new HashMap<>();

        bindingStrategyMap.put(KakaoBookProvider.class, new KakaoBookBinding());
        bindingStrategyMap.put(LoanableLibProvider.class, new LoanableLibBinding());
    }

    /**
     * Api 응답 결과를 객체로 바인딩 하기 위해 해당 Binding Startegy 객체를 Map에서 찾아 Binding 한 후 반환
     *
     * @param apiResponse Api의 Json 형식의 응답 데이터
     * @param provider    해당 API와 통신하여 데이터를 제공하는 Provider
     * @return API 응답 결과가 바인딩 되는 객체
     * @throws OpenApiException API와의 연결이 문제가 있을 때 던져 진다.
     */
    @SuppressWarnings("unchecked")
    public T bind(ResponseEntity<String> apiResponse,
        Class<? extends DataProvider<T>> provider
    ) throws OpenApiException {

        return (T) bindingStrategyMap.get(provider).bind(apiResponse);
    }

    public List<T> bindList(@NonNull List<ResponseEntity<String>> apiResponses,
        Class<? extends DataProvider<T>> provider
    ) throws OpenApiException {

        return apiResponses.stream().map(r -> bind(r, provider)).toList();
    }

}
