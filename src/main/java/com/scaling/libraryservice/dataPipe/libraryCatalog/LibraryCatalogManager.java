package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.dataPipe.csv.util.CsvFileMerger;
import com.scaling.libraryservice.dataPipe.download.LibraryCatalogDownloader;
import java.io.IOException;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryCatalogManager {

    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;

    private final LibraryCatalogNormalizer normalizer;

    private final LibraryCatalogDownloader libraryCatalogDownloader;

    private final static String ROOT = "pipe";

    private final static String BRANCH = "step";

    // targetDate "(2023년 06월)"
    @BatchLogging
    public void executeProcess(String targetDate) throws IOException {

        Path downLoadFolder = executeFilDownload(targetDate);

        Path normalizePath = executeNormalizeStep(downLoadFolder);

        Path divideStep = executeDivideStep(normalizePath);

        Path aggregateStep = executeAggregateStep(divideStep);

        executeMergeStep(aggregateStep);
    }

    private Path executeNormalizeStep(Path downLoadFolder) {
        return normalizer.normalize(
            downLoadFolder.toString(),
            configOutPut(1, "normalFile.csv")
        );
    }

    private Path executeFilDownload(String targetDate) {
        return libraryCatalogDownloader.downLoad(ROOT + "/download/", targetDate);
    }


    private Path executeDivideStep(Path normalizePath) {
        return LibraryCatalogDivider.divide(
            normalizePath.toString(),
            configOutPut(2, "divide"),
            10000000
        );
    }

    private Path executeAggregateStep(Path divideStep) {

        return LibraryCatalogAggregator.aggregateLoanCnt(
            divideStep.subpath(0,2).toString(),
            configOutPut(3, "aggregatedFile"),
            "%s_%d.csv"
        );
    }

    private void executeMergeStep(Path aggregateStep) throws IOException {
        CsvFileMerger.mergeCsvFile(
            aggregateStep.subpath(0,2).toString(),
            configOutPut(4, "result"),
            ISBN_IDX, LOAN_CNT_IDX
        );
    }

    private String configOutPut(int number, String leafNm) {
        return String.format("%s/%s%d/%s",ROOT,BRANCH,number,leafNm);
    }

}
