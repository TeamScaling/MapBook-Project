package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import com.scaling.libraryservice.dataPipe.csv.util.CsvFileMerger;
import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MergingStep implements ExecutionStep{

    // pipe/mergingStep
    private final String outPutFileNm;
    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;

    public MergingStep(String outPutFileNm) {
        this.outPutFileNm = outPutFileNm;
    }

    @Override
    public Path execute(Path input) throws IOException {
        log.info("csvFileMerger input [{}]",input.subpath(0,2));
        return CsvFileMerger.mergeCsvFile(
            input.subpath(0,2).toString(),
            outPutFileNm,
            null,
            ISBN_IDX, LOAN_CNT_IDX
        );
    }

}
