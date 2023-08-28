package com.scaling.libraryservice.batch.preSortBook.config;

import com.scaling.libraryservice.batch.preSortBook.chunk.PreSortBookItemProcessor;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.entity.SortBook;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class PreSortBookBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final TitleAnalyzer titleAnalyzer;

    private final int CHUNK_SIZE = 10000;

    private final Job keyWordJob;

    @Bean
    public Job preSortByBooksJob() {
        return jobBuilderFactory.get("preSortByBooksJob")
            .start(insertSortBooksStep())
            .next(keywordJopStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step insertSortBooksStep() {
        return stepBuilderFactory.get("insertSortBooksStep")
            .<Book, SortBook>chunk(CHUNK_SIZE)
            .reader(bookJpaPagingItemReader())
            .processor(preSortBookItemProcessor())
            .writer(jpaItemWriter())
            .build();
    }

    @Bean
    public Step keywordJopStep(){
        return stepBuilderFactory.get("keywordJopStep")
            .job(keyWordJob)
            .build();
    }

    @Bean
    public ItemProcessor<Book, SortBook> preSortBookItemProcessor() {
        return new PreSortBookItemProcessor(titleAnalyzer);
    }

    public JpaItemWriter<SortBook> jpaItemWriter() {
        return new JpaItemWriterBuilder<SortBook>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

    public JpaPagingItemReader<Book> bookJpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Book>()
            .name("bookJpaPagingItemReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .queryString("select b from Book b order by b.loanCnt desc")
            .build();
    }
}
