package com.scaling.libraryservice.config;

import com.scaling.libraryservice.search.util.recommend.RecommendRanRule;
import com.scaling.libraryservice.search.util.recommend.RecommendRule;
import com.scaling.libraryservice.search.util.relate.RelationTokenRule;
import com.scaling.libraryservice.search.util.relate.RelationRule;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.scaling.libraryservice.mapBook.util")
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return new RestTemplate(factory);
    }

    @Bean
    public Komoran komoran() {

        return new Komoran(DEFAULT_MODEL.FULL);
    }
    @Bean
    public RelationRule relBookPolicy() {

        return new RelationTokenRule();
    }

    @Bean
    public RecommendRule recommendPolicy() {

        return new RecommendRanRule();
    }


}
