package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogDivider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;


public class DivideStep implements ExecutionStep {

    //"pipe/divideStep/divide"
    private final String outPutFileNm;
    private final long maxRecordPerFile;

    public DivideStep(String outPutFileNm, long maxRecordPerFile) {
        this.outPutFileNm = outPutFileNm;
        this.maxRecordPerFile = maxRecordPerFile;
    }

    @Override
    public Path execute(Path input) throws IOException {
        return LibraryCatalogDivider.divide(
            input.toString(),
            outPutFileNm,
            maxRecordPerFile
        );
    }

}
