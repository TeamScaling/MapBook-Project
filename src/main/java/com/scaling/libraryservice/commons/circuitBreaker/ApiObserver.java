package com.scaling.libraryservice.commons.circuitBreaker;

/**
 *  이 인터페이스 구현체는 ApiStatus를 멤버로 가지며, {@link CircuitBreaker}
 *  에게 서버 장애 관련해서 관리 받을 수 있는 인터페이스
 */
public interface ApiObserver {

    ApiStatus getApiStatus();

}
