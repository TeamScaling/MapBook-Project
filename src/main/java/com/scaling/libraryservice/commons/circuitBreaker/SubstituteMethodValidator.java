package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.commons.circuitBreaker.exception.SubstituteMethodException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SubstituteMethodValidator implements BeanPostProcessor {

    private final CircuitBreakerSupporter circuitBreakerSupporter;

    @Override // @ApiMonitoring에서 substitute()에 매칭 되는 메소드가 없는 경우 서버를 시작하며 에러를 발생
    public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) {

        Method[] methods = bean.getClass().getMethods();

        Arrays.stream(methods).forEach(method -> {
            ApiMonitoring apiMonitoring = method.getAnnotation(ApiMonitoring.class);

            if (apiMonitoring != null) {
                try {
                    circuitBreakerSupporter.extractSubstituteMethod(apiMonitoring, methods);
                } catch (IllegalArgumentException e) {

                    throw new SubstituteMethodException(
                        "Invalid substitute method name "
                            + apiMonitoring.substitute()
                            + " in bean "
                            + beanName
                    );
                }
            }}
        );

        return bean;
    }
}
