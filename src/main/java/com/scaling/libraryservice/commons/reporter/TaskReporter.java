package com.scaling.libraryservice.commons.reporter;

/**
 * {@link TaskReporter} 인터페이스는 작업 보고 기능을 정의합니다.
 * 구현 클래스는 보고할 메시지를 받아 필요한 형태로 보고하도록 구현해야 합니다.
 */
public interface TaskReporter {

    /**
     * 주어진 메시지를 보고하는 메소드입니다.
     *
     * @param message 보고할 메시지
     */
    void report(String message);
}
