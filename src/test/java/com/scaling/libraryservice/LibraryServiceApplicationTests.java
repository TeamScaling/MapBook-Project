package com.scaling.libraryservice;

import static com.scaling.libraryservice.dataPipe.download.LibraryCatalogDownloader.getDefaultDirectory;

import com.scaling.libraryservice.dataPipe.csv.exporter.BookExporter;
import com.scaling.libraryservice.dataPipe.download.LibraryCatalogDownloader;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogExecutor;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogNormalizer;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.AggregatingStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.DivideStep;
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


    @Test
    public void execute_pipe() {
        /* given */

        DownLoadStep downLoadStep = new DownLoadStep(
            libraryCatalogDownloader,
            "(2023년 06월)",
            true,
            30
        );

        StepBuilder stepBuilder = new StepBuilder();
        List<ExecutionStep> executionSteps = stepBuilder
            .start(downLoadStep)
            .next(new NormalizeStep("pipe/normalizeStep/normalFile.csv"))
            .next(new DivideStep("pipe/divideStep", 10000000))
            .next(new AggregatingStep("pipe/aggregatingStep/aggregating"))
            .next(new MergingStep("pipe/mergingStep/mergeFile.csv"))
            .next(new AggregatingStep("pipe/endStep/end"))
            .end();

        libraryCatalogExecutor.executeProcess(
            Path.of("pipe/download/"),
            executionSteps,
            downLoadStep
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
    public void collectLoanCnt() {
        /* given */

        libraryCatalogDownloader.downLoad(getDefaultDirectory(), "(2023년 06월)", false, 0);
        /* when */

        /* then */
    }

    // bookVo object를 Csv 파일로 output 한다.
    public void exportToCsv() {
        bookExporter.exportToCsv(0, 500000, "bookAuthr.csv", false);
    }


}
