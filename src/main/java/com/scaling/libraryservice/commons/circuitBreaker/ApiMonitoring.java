package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ApiMonitoring} 어노테이션은 메소드에 적용되어 원본 메소드를 대체하는 메소드임을 나타냅니다.
 * 이 어노테이션은 대체 메소드와 원본 메소드 간의 관계를 표현하고, 선택적으로 대체 메소드의 이름을 지정할 수 있습니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMonitoring {

    /**
     * 대체될 원본 메소드가 속한 클래스를 반환합니다.
     *
     * @return 원본 메소드가 속한 클래스
     */
    Class<? extends ApiObserver> api();

    /**
     * 대체 메소드의 이름을 지정합니다.
     *
     * @return 대체 메소드의 이름
     */
    String substitute();

}
