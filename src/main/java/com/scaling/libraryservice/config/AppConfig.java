package com.scaling.libraryservice.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scaling.libraryservice.commons.api.util.ApiQuerySender;
import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import com.scaling.libraryservice.commons.circuitBreaker.CircuitBreaker;
import com.scaling.libraryservice.commons.circuitBreaker.restoration.ApiQuerySendChecker;
import com.scaling.libraryservice.commons.circuitBreaker.restoration.RestorationChecker;
import com.scaling.libraryservice.logging.logger.OpenApiSlackLogger;
import com.scaling.libraryservice.logging.service.LogService;
import com.scaling.libraryservice.search.engine.filter.StopWordFilter;
import com.scaling.libraryservice.search.service.KeywordService;
import com.scaling.libraryservice.search.engine.filter.ConvertFilter;
import com.scaling.libraryservice.search.engine.filter.FilterStream;
import com.scaling.libraryservice.search.engine.filter.SimpleFilter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class AppConfig {

    private final JobRegistry jobRegistry;

    private final BasicBatchConfigurer basicBatchConfigurer;

    @Bean
    public CircuitBreaker circuitBreaker(RestorationChecker restorationChecker,
        LogService<ApiStatus> logService) {

        return new CircuitBreaker(
            Executors.newScheduledThreadPool(1),
            new ConcurrentHashMap<>(),
            restorationChecker,
            logService);
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
                ,true));
    }

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor(){

        JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
        beanPostProcessor.setJobRegistry(jobRegistry);

        return beanPostProcessor;
    }

    @Bean
    public SimpleJobLauncher simpleJobLauncher(){

        SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher)basicBatchConfigurer.getJobLauncher();
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return simpleJobLauncher;
    }


}

