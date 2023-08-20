package com.scaling.libraryservice.commons.api.util.binding;

import com.scaling.libraryservice.batch.logTransfer.mappingStratagy.MappingStrategy;
import com.scaling.libraryservice.commons.api.service.provider.KakaoBookProvider;
import com.scaling.libraryservice.commons.api.service.provider.LoanableLibProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BindingStrategyFactoryBean implements FactoryBean<Map<Class<?>, BindingStrategy<?>>> {

    @Value("${binding-strategies}")
    private String[] strategyMappings;

    @Nullable
    @Override
    public Map<Class<?>, BindingStrategy<?>> getObject() {

        Map<Class<?>, BindingStrategy<?>> bindingStrategyMap = new HashMap<>();

        for (String clazz : strategyMappings){
            switch (clazz){
                case "KakaoBookProvider" ->
                    bindingStrategyMap.put(KakaoBookProvider.class,new KakaoBookBinding());
                case "LoanableLibProvider" ->
                    bindingStrategyMap.put(LoanableLibProvider.class,new LoanableLibBinding());
                default ->
                    throw new IllegalArgumentException("Invalid binding strategy: "+clazz);
            }
        }
        return bindingStrategyMap;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
