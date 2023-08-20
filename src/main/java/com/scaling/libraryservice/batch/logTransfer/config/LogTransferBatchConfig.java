package com.scaling.libraryservice.batch.logTransfer.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaling.libraryservice.batch.logTransfer.chunk.LogItemProcessor;
import com.scaling.libraryservice.batch.logTransfer.chunk.LogVoItemReader;
import com.scaling.libraryservice.batch.logTransfer.entity.SlackLog;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@RequiredArgsConstructor
@Configuration
public class LogTransferBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LogItemProcessor logItemProcessor;

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job logTransferJob() {
        return jobBuilderFactory.get("logTransferJob")
            .start(transferStep(null))
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    @JobScope
    public Step transferStep(@Value("#{jobParameters['inputFolder']}") String inputFolder) {
        return stepBuilderFactory.get("transferStep")
            .<SlackLogVo,SlackLog>chunk(100)
            .reader(multiResourceItemReader(inputFolder))
            .processor(logItemProcessor)
            .writer(jpaItemWriter())
            .listener(stepExecutionListener())
            .build();
    }

    @Bean
    @StepScope
    public ResourceAwareItemReaderItemStream<SlackLogVo> itemReader(){
        return new LogVoItemReader(jsonItemReader());
    }


    private MultiResourceItemReader<SlackLogVo> multiResourceItemReader(String inputFolder) {
        return new MultiResourceItemReaderBuilder<SlackLogVo>()
            .name("SlackLogMultiReader")
            .delegate(itemReader())
            .resources(getResources(inputFolder))
            .build();
    }

    private JsonItemReader<SlackLogVo> jsonItemReader(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        JacksonJsonObjectReader<SlackLogVo> reader = new JacksonJsonObjectReader<>(SlackLogVo.class);
        reader.setMapper(mapper);

        return new JsonItemReaderBuilder<SlackLogVo>()
            .name("reader")
            .jsonObjectReader(reader)
            .build();
    }

    private StepExecutionListener stepExecutionListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {

            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                logItemProcessor.clear();
                return ExitStatus.COMPLETED;
            }
        };
    }

    private Resource[] getResources(String folderNm) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            return resolver.getResources("file:" + folderNm + "/*.json");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private JpaItemWriter<SlackLog> jpaItemWriter() {
        return new JpaItemWriterBuilder<SlackLog>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }


}
