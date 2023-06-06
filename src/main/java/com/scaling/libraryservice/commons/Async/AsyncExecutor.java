package com.scaling.libraryservice.commons.Async;

import java.util.function.Supplier;

/**
 * AsyncExecutor 인터페이스는 비동기 실행을 위한 메서드를 정의합니다.
 * 이 인터페이스는 결과 타입이 T이고, 실행에 필요한 값의 타입이 V인 작업을 정의합니다.
 *
 * @param <T> 실행 결과의 타입
 * @param <V> 실행에 필요한 값의 타입
 */
public interface AsyncExecutor<T,V> {

    /**
     * 주어진 Supplier와 값을 이용하여 비동기 작업을 실행합니다.
     * 이 메서드는 작업의 실행 결과를 반환하며, 제한 시간이 초과하면 예외를 발생시킬 수 있습니다.
     *
     * @param supplier 비동기 작업을 제공하는 Supplier
     * @param value 작업 실행에 필요한 값
     * @param timeout 작업의 최대 실행 시간(단위: 초)
     * @return 작업의 실행 결과
     */
    T execute(Supplier<T> supplier,V value,int timeout);
}
