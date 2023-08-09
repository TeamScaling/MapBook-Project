package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.commons.circuitBreaker.exception.SubstituteMethodException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component @Slf4j
public class SubstituteMethodValidator implements BeanPostProcessor {

    private final CircuitBreakerSupporter circuitBreakerSupporter;

    @Override // @ApiMonitoring에서 substitute()에 매칭 되는 메소드가 없는 경우 에러를 발생하며 서버 실행 X
    public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) {

        Arrays.stream(bean.getClass().getMethods())
            .filter(this::isRelatedApiMonitoring)
            .forEach(method -> {
                ApiMonitoring apiMonitoring = method.getAnnotation(ApiMonitoring.class);
                validateSubstituteMethod(apiMonitoring, beanName,bean.getClass().getMethods());
            });
        return bean;
    }


    private boolean isRelatedApiMonitoring(Method method) {
        return method.getAnnotation(ApiMonitoring.class) != null;
    }

    private void validateSubstituteMethod(
        ApiMonitoring apiMonitoring, String beanName, Method[] methods
    ) {
        try {
            Method substituteMethod
                = circuitBreakerSupporter.getSubstituteMethod(apiMonitoring, methods);

            log.info(
                "[{}] has successfully found the substituteMethod [{}] in [{}]",
                this.getClass().getSimpleName(),
                substituteMethod.getName(),
                beanName
            );
        } catch (IllegalArgumentException e) {
            throw new SubstituteMethodException(
                "Invalid substitute method name "
                    + apiMonitoring.substitute()
                    + " in bean "+beanName
            );
        }
    }
}
