package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link QuerySendChecker} 클래스는 {@link RestorationChecker} 인터페이스를 구현하여
 * API 서버의 상태를 확인하고, 해당 API 서버가 접근 가능한 상태로 복원될 수 있는지 확인합니다.
 * 이 클래스는 {@link ApiQuerySender} 인스턴스를 사용하여 API 서버에 테스트 쿼리를 보냅니다.
 *
 * 테스트 쿼리가 성공하면 API 서버가 이제 접근 가능하다는 것을 의미하고, 메소드는 true를 반환합니다.
 * 쿼리를 보내는 동안 예외가 발생하면 API 서버는 여전히 접근할 수 없는 상태라는 것을 의미하며, 이 경우 false를 반환합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class QuerySendChecker implements RestorationChecker{

    private final ApiQuerySender apiQuerySender;

    /**
     * {@link ApiConnection} 인스턴스를 사용하여 해당 API 서버의 복원 가능성을 확인하는 메소드입니다. 이 메소드는
     * {@link ApiConnection}가 가리키는 API 서버가 다시 접근 가능한 상태인지를 확인하기 위해 테스트 쿼리를 전송하고 그 결과를 반환합니다.
     *
     * @param observer 복원 가능성을 확인할 API 서버와의 연결 구성 요소를 갖는 {@link ApiObserver} 인스턴스
     * @return API 서버가 다시 접근 가능한 상태라면 true, 그렇지 않은 경우 false를 반환합니다.
     */
    public boolean isRestoration(ApiObserver observer) {

        String uri = observer.getApiStatus().getApiUri();

        log.info("check restoration of Api [{}] at [{}]", uri,LocalDateTime.now());

        try {
            apiQuerySender.sendSingleQuery(() -> UriComponentsBuilder.fromHttpUrl(uri), HttpEntity.EMPTY);
        } catch (OpenApiException e) {
            return false;
        }

        return true;
    }
}
