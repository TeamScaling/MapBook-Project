package com.scaling.libraryservice.batch.loanCnt.config;

import com.scaling.libraryservice.batch.loanCnt.chunk.DownLoadFileClearTask;
import com.scaling.libraryservice.batch.loanCnt.chunk.LibraryCatalogTokenizer;
import com.scaling.libraryservice.batch.loanCnt.chunk.LibraryCatalogWriter;
import com.scaling.libraryservice.batch.loanCnt.domain.LibraryCatalog;
import com.scaling.libraryservice.batch.loanCnt.util.Aggregator;
import com.scaling.libraryservice.batch.loanCnt.util.download.LibraryCatalogDownloader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@RequiredArgsConstructor
@Configuration
public class LoanCntBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LibraryCatalogDownloader libraryCatalogDownloader;
    private Aggregator aggregator;
    private final LibraryCatalogWriter libraryCatalogWriter;
    private final DownLoadFileClearTask downLoadFileClearTask;

    private final String DOWNLOAD_FOLDER = "src/main/resources/pipe/download";

    private final String RESULT_PATH = "src/main/resources/pipe/endStep/end.csv";

    @Bean
    public Job aggregatingJob() {
        return jobBuilderFactory.get("aggregatingJob")
            .start(downloadStep(null))
            .next(summarizeLoanCountsByIsbnStep())
            .next(transferAggregatedDataToDbStep())
            .next(fileClearStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    @JobScope
    public Step downloadStep(@Value("#{jobParameters['downLoadDate']}") String downLoadDate) {

        String targetDate = downLoadDate.replaceAll("\"", "");

        return stepBuilderFactory.get("downloadStep")
            .tasklet((contribution, chunkContext) -> {
                libraryCatalogDownloader.downLoad(
                    DOWNLOAD_FOLDER, targetDate, false, -1
                );
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step summarizeLoanCountsByIsbnStep() {
        return stepBuilderFactory.get("summarizeLoanCountsByIsbnStep")
            .<LibraryCatalog, LibraryCatalog>chunk(100000)
            .reader(multiResourceItemReader())
            .writer(items -> items.forEach(item -> aggregator.aggregateLoanCnt(item)))
            .faultTolerant()
            .skip(FlatFileParseException.class)
            .skipLimit(1000)
            .listener(stepExecutionListener())
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                aggregator = new Aggregator(new HashMap<>(3000000));
            }

            @Nullable
            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                List<LibraryCatalog> libraryCatalogs = aggregator.getAggregatedLibraryCatalogs();
                FlatFileItemWriter<LibraryCatalog> catalogFlatFileItemWriter
                    = catalogFlatFileItemWriter(RESULT_PATH);

                try {
                    catalogFlatFileItemWriter.open(stepExecution.getExecutionContext());
                    catalogFlatFileItemWriter.write(libraryCatalogs);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    catalogFlatFileItemWriter.close();
                }
                return null;
            }
        };
    }

    @Bean
    public Step transferAggregatedDataToDbStep() {
        return stepBuilderFactory.get("transferAggregatedDataToDbStep")
            .<LibraryCatalog, LibraryCatalog>chunk(100000)
            .reader(aggregatedFileReader(RESULT_PATH))
            .writer(libraryCatalogWriter)
            .taskExecutor(taskExecutor())
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("BatchExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Step fileClearStep() {
        return stepBuilderFactory.get("clearStep")
            .tasklet(downLoadFileClearTask)
            .build();
    }

    private FlatFileItemReader<LibraryCatalog> aggregatedFileReader(String path) {
        return new FlatFileItemReaderBuilder<LibraryCatalog>()
            .name("flatFileLibCatalog")
            .resource(new FileSystemResource(path))
            .delimited().delimiter(",")
            .names("isbn", "loanCnt")
            .targetType(LibraryCatalog.class)
            .linesToSkip(1)
            .build();
    }

    @Bean
    public FlatFileItemReader<LibraryCatalog> catalogFlatFileItemReader() {
        return new FlatFileItemReaderBuilder<LibraryCatalog>()
            .name("catalogReader")
            .encoding("EUC-KR")
            .linesToSkip(1)
            .lineTokenizer(customDelimitedLineTokenizer())
            .fieldSetMapper(customFieldSepMapper())
            .build();
    }

    private FlatFileItemWriter<LibraryCatalog> catalogFlatFileItemWriter(String path) {
        return new FlatFileItemWriterBuilder<LibraryCatalog>()
            .name("loanWriter")
            .delimited()
            .names("isbn", "loanCnt")
            .resource(new FileSystemResource(path))
            .build();
    }

    private DelimitedLineTokenizer customDelimitedLineTokenizer() {

        DelimitedLineTokenizer tokenizer = new LibraryCatalogTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setQuoteCharacter('\"');
        tokenizer.setNames("번호", "도서명", "저자", "출판사", "발행년도", "ISBN", "세트 ISBN", "부가기호", "권",
            "주제분류번호", "도서권수", "대출건수", "등록일자", "");
        tokenizer.setStrict(false);
        return tokenizer;
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<LibraryCatalog> multiResourceItemReader() {
        return new MultiResourceItemReaderBuilder<LibraryCatalog>()
            .name("libraryCatalogMultiReader")
            .delegate(catalogFlatFileItemReader())
            .resources(getResources(DOWNLOAD_FOLDER))
            .build();
    }


    private Resource[] getResources(String folderNm) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            return resolver.getResources("file:" + folderNm + "/*.csv");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private FieldSetMapper<LibraryCatalog> customFieldSepMapper() {
        return fieldSet -> fieldSet.getFieldCount() == 14 ?
            new LibraryCatalog(fieldSet.readRawString(5), fieldSet.readInt("대출건수")) :
            new LibraryCatalog("0", 0);
    }


}
