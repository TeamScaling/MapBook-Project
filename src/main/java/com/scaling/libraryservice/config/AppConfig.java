package com.scaling.libraryservice.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker;
import com.scaling.libraryservice.commons.circuitBreaker.restoration.ApiQuerySendChecker;
import com.scaling.libraryservice.commons.circuitBreaker.restoration.RestorationChecker;
import com.scaling.libraryservice.logging.logger.OpenApiLogger;
import com.scaling.libraryservice.search.engine.filter.StopWordFilter;
import com.scaling.libraryservice.search.service.KeywordService;
import com.scaling.libraryservice.search.engine.filter.ConvertFilter;
import com.scaling.libraryservice.search.engine.filter.FilterStream;
import com.scaling.libraryservice.search.engine.filter.SimpleFilter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public CircuitBreaker circuitBreaker(RestorationChecker restorationChecker,
        OpenApiLogger openApiLogger) {

        return new CircuitBreaker(
            Executors.newScheduledThreadPool(1),
            new ConcurrentHashMap<>(),
            restorationChecker,
            openApiLogger);
    }

    @Bean
    public RestTemplate restTemplateTimeOut() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);

        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Bean
    public ApiQuerySender apiQuerySenderTimeOut() {

        return new ApiQuerySender(restTemplateTimeOut());
    }

    @Bean
    public ApiQuerySender apiQuerySender() {

        return new ApiQuerySender(restTemplate());
    }


    @Bean
    public RestorationChecker restorationChecker() {

        return new ApiQuerySendChecker(apiQuerySenderTimeOut());
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {

        return new JPAQueryFactory(em);
    }

    @Bean
    public FilterStream filterStream(KeywordService keywordService) {
        return new FilterStream(
            new SimpleFilter(
                new StopWordFilter(
                    new ConvertFilter(null, keywordService))
                ));
    }


}

