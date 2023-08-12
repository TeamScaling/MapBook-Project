package com.scaling.libraryservice;

import static com.scaling.libraryservice.dataPipe.libraryCatalog.download.LibraryCatalogDownloader.getDefaultDirectory;

import com.scaling.libraryservice.dataPipe.FileService;
import com.scaling.libraryservice.dataPipe.csv.exporter.BookExporter;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogAggregator;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogNormalizer;
import com.scaling.libraryservice.dataPipe.libraryCatalog.download.LibraryCatalogDownloader;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogExecutor;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.AggregatingStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.DownLoadStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.ExecutionStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.MergingStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.NormalizeStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.StepBuilder;
import com.scaling.libraryservice.dataPipe.updater.service.BookUpdateService;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private BookUpdateService bookUpdateService;

    @Autowired
    LibraryCatalogDownloader libraryCatalogDownloader;


    @Autowired
    BookExporter bookExporter;

    @Autowired
    TitleAnalyzer analyzer;

    @Autowired
    LibraryCatalogExecutor libraryCatalogExecutor;

    @Autowired
    LibraryCatalogAggregator libraryCatalogAggregator;

    @Autowired
    LibraryCatalogNormalizer libraryCatalogNormalizer;


    @Autowired
    FileService fileService;


    public void execute_pipe() {
        /* given */

        DownLoadStep downLoadStep = new DownLoadStep(
            libraryCatalogDownloader,
            "(2023년 07월)",
            true,
            50
        );

        NormalizeStep normalizeStep = new NormalizeStep("pipe/normalizeStep",
            libraryCatalogNormalizer,
            fileService);
        AggregatingStep aggregatingStep = new AggregatingStep(
            "pipe/normalizeStep",
            "pipe/aggregatingStep/aggregate",
            10,
            libraryCatalogAggregator,
            fileService);

        StepBuilder stepBuilder = new StepBuilder();
        List<ExecutionStep> executionSteps = stepBuilder
            .start(downLoadStep)
            .next(normalizeStep)
            .next(aggregatingStep)
            .next(new MergingStep("pipe/mergingStep/mergedFile.csv"))
            .next(aggregatingStep)
            .end();

        libraryCatalogExecutor.executeProcess(
            Path.of("pipe/normalizeStep"),
            executionSteps
        );
    }


    public void libMerger() {

    }

    @DisplayName("도서 업데이트 메소드 실행. 테스트 메소드가 아닌 실행을 위한 메소드")
    public void execute_update() {

        bookUpdateService.UpdateBookFromApi(200, 100);
    }

    //작업용
    //다운 로드한 Csv 파일을 하나로 합친다.
    public void mergeLibraryData() {
        /* given */

        /* when */

        /* then */
    }

    // 크롤러를 통해 원하는 날짜의 대출 횟수를 자동 다운로드 한다.
    @Test
    public void collectLoanCnt() throws IOException {
        /* given */

        AggregatingStep aggregatingStep = new AggregatingStep(
            "pipe/normalizeStep",
            "pipe/aggregatingStep/aggregate",
            3,
            libraryCatalogAggregator,
            fileService
        );

        aggregatingStep.execute(Path.of("pipe"));

        /* when */

        /* then */
    }

    // bookVo object를 Csv 파일로 output 한다.
    public void exportToCsv() {
        bookExporter.exportToCsv(0, 500000, "bookAuthr.csv", false);
    }

    @Test
    public void normalize() throws IOException {
        NormalizeStep step = new NormalizeStep("pipe/normalizeStep/",libraryCatalogNormalizer,fileService);
        step.execute(Path.of("pipe/download"));
    }


}
