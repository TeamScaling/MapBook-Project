package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import com.scaling.libraryservice.logging.util.parser.SlackLogParser;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MappingStrategyFactoryBean implements FactoryBean<Map<Integer, MappingStrategy>> {

    @Value("${mapping-strategies}")
    private String[] strategyMappings;

    private final SlackLogParser slackLogParser;

    @Override
    public Map<Integer, MappingStrategy> getObject(){
        Map<Integer, MappingStrategy> mappingStrategyMap = new HashMap<>();
        
        for (String mapping : strategyMappings) {
            String[] parts = mapping.split(":");
            Integer code = Integer.parseInt(parts[0]);

            switch (parts[1]) {
                case "SearchMapping" ->
                    mappingStrategyMap.put(code, new SearchMapping(slackLogParser));
                case "ApiErrorMapping" ->
                    mappingStrategyMap.put(code, new ApiErrorMapping(slackLogParser));
                case "MapBookMapping" ->
                    mappingStrategyMap.put(code, new MapBookMapping(slackLogParser));
                default ->
                    throw new IllegalArgumentException("Invalid mapping strategy: " + parts[1]);
            }
        }

        return mappingStrategyMap;
    }

    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
