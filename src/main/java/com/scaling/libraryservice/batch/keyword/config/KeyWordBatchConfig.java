package com.scaling.libraryservice.batch.keyword.config;

import com.scaling.libraryservice.batch.keyword.chunk.KeywordItemWriter;
import com.scaling.libraryservice.search.engine.filter.SimpleFilter;
import com.scaling.libraryservice.search.engine.filter.StopWordFilter;
import com.scaling.libraryservice.search.entity.Keyword;
import com.scaling.libraryservice.search.entity.SortBook;
import com.scaling.libraryservice.search.repository.KeywordRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeyWordBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private SimpleFilter simpleFilter;

    private final EntityManagerFactory entityManagerFactory;

    private final KeywordItemWriter keywordItemWriter;

    private Set<Keyword> bookTokens;

    @Bean
    public Job keyWordJob() {
        return jobBuilderFactory.get("keyWordJob")
            .start(keywordStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step keywordStep() {
        return stepBuilderFactory.get("keywordStep")
            .<SortBook, Set<Keyword>>chunk(1000)
            .reader(bookJpaPagingItemReader())
            .processor(itemProcessor())
            .writer(items ->
                items.forEach(item ->
                    bookTokens.addAll(item)
                )
            )
            .listener(stepExecutionListener())
            .build();
    }



    private StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                bookTokens = new HashSet<>();
                simpleFilter = new SimpleFilter(new StopWordFilter(null), false);
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                keywordItemWriter.write(bookTokens.stream().toList());
                return ExitStatus.COMPLETED;
            }
        };
    }

    private ItemProcessor<SortBook, Set<Keyword>> itemProcessor() {
        return item -> Arrays.stream(item.getTitleToken().split(" "))
            .map(simpleFilter::filtering)
            .filter(token -> !token.isBlank())
            .map(Keyword::new)
            .collect(Collectors.toSet());
    }


    private JpaPagingItemReader<SortBook> bookJpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<SortBook>()
            .name("bookJpaPagingItemReader")
            .pageSize(1000)
            .entityManagerFactory(entityManagerFactory)
            .queryString("select b from SortBook b")
            .build();
    }


}
