package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MappingStrategyFactory {

    private Map<Integer, MappingStrategy> mappingStrategyMap;
    private final MappingStrategyFactoryBean mappingStrategyFactoryBean;

    @PostConstruct
    private void setUp(){
        this.mappingStrategyMap = mappingStrategyFactoryBean.getObject();
    }

    public MappingStrategy getMappingStrategy(Integer taskCode){
        if (!mappingStrategyMap.containsKey(taskCode)){
            throw new IllegalArgumentException("It is fail to find taskCod in strategyMap");
        }

        return mappingStrategyMap.get(taskCode);
    }

}
