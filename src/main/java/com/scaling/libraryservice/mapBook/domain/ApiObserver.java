package com.scaling.libraryservice.mapBook.domain;

import com.scaling.libraryservice.mapBook.dto.ApiStatus;

/**
 *  이 인터페이스 구현체는 ApiStatus를 멤버로 가지며, {@link com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker}
 *  에게 서버 장애 관련해서 관리 받을 수 있다.
 */
public interface ApiObserver {

    ApiStatus getApiStatus();


}
