package com.scaling.libraryservice.commons.circuitBreaker;

/**
 * RestorationChecker는 특정 API 호출의 복구 가능성을 확인하는 역할을 합니다.
 * 이 인터페이스는 API 호출이 실패했을 때, 이를 복구할 수 있는지 여부를 판단하는 메서드를 정의합니다.
 *
 * @see ApiObserver
 */
public interface RestorationChecker {

    /**
     * 주어진 {@link ApiObserver}를 사용하여 API 호출의 복구 가능성을 확인합니다.
     * 이 메서드는 API 호출이 실패했을 때 호출되며, 이를 통해 실패한 API 호출을 복구할 수 있는지 여부를 반환합니다.
     *
     * @param observer 실패한 API 호출에 대한 관찰자
     * @return API 호출을 복구할 수 있는지 여부를 나타내는 boolean 값. 복구가 가능하면 true, 그렇지 않으면 false를 반환합니다.
     */
    boolean isRestoration(ApiObserver observer);
}
