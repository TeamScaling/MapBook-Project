package com.scaling.libraryservice.commons.api.util.binding;

import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BindingStrategyFactory {

    private Map<Class<?>, BindingStrategy<?>> bindingStrategyMap;

    private final BindingStrategyFactoryBean bindingStrategyFactoryBean;

    @PostConstruct
    private void setUp(){
        bindingStrategyMap = bindingStrategyFactoryBean.getObject();
    }

    public BindingStrategy<?> getBindingStrategy(Class<?> clazz){

        if(!bindingStrategyMap.containsKey(clazz)){
            throw new IllegalArgumentException("It is fail to Find BidingStrategy in Map");
        }

        return bindingStrategyMap.get(clazz);
    }

}
