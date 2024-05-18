package com.scaling.libraryservice.batch.bookUpdate.config;

import com.scaling.libraryservice.batch.bookUpdate.chunk.BookUpdateWriter;
import com.scaling.libraryservice.batch.bookUpdate.entity.RequiredUpdateBook;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.RequiredUpdateBookRepo;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BookUpdateBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final BookUpdateWriter bookUpdateWriter;

    private final RequiredUpdateBookRepo requiredUpdateBookRepo;

    private final int CHUNK_SIZE = 100;
    private final int MAX_ITEM_SIZE = 30000;

    private final Job preSortByBooksJob;

    @Bean
    public Job bookUpdateJob() {
        return jobBuilderFactory.get("updateJob")
            .start(checkingStep())// update가 필요한지 판단
                .on("NOT_REQUIRED_UPDATE").to(insertUpdatedData2BooksStep())
                .from(insertUpdatedData2BooksStep()).on("COMPLETE").to(preSortByBooksJobStep())
                .from(preSortByBooksJobStep()).on("COMPLETE").end()
                .from(checkingStep()).on("*").to(updateStep()) // update 진행
                .end()
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step checkingStep() {
        return stepBuilderFactory.get("checkingStep")
            .tasklet((contribution, chunkContext) -> {
                if (requiredUpdateBookRepo.countByNotFound(false) == 0) {
                    contribution.getStepExecution().setExitStatus(
                        new ExitStatus("NOT_REQUIRED_UPDATE")
                    );
                }
                return RepeatStatus.FINISHED;})
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step updateStep() {
        return stepBuilderFactory.get("updateStep")
            .<RequiredUpdateBook, RequiredUpdateBook>chunk(CHUNK_SIZE)
            .reader(jpaPagingItemReader("select b from RequiredUpdateBook b where b.notFound = false"))
            .writer(bookUpdateWriter)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step preSortByBooksJobStep(){
        return stepBuilderFactory.get("preSortByBooksJobStep")
            .job(preSortByBooksJob)
            .allowStartIfComplete(true)
            .build();
    }

    // 최신화가 완료된 데이터를 기존 Book table에 insert
    @Bean
    public Step insertUpdatedData2BooksStep(){
        return stepBuilderFactory.get("insertUpdatedData2BooksStep")
            .<RequiredUpdateBook,Book>chunk(10000)
            .reader(jpaPagingItemReader("select b from RequiredUpdateBook b where b.title is not null"))
            .processor((ItemProcessor<RequiredUpdateBook, Book>) Book::new)
            .writer(jpaItemWriter())
            .allowStartIfComplete(true)
            .build();
    }


    private JpaPagingItemReader<RequiredUpdateBook> jpaPagingItemReader(String query) {
        return new JpaPagingItemReaderBuilder<RequiredUpdateBook>()
            .name("requiredUpdateJpaReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .maxItemCount(MAX_ITEM_SIZE)
            .queryString(query)
            .build();
    }

    private JpaItemWriter<Book> jpaItemWriter() {
        return new JpaItemWriterBuilder<Book>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

}
