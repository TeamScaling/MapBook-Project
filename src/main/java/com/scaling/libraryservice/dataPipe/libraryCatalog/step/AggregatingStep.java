package com.scaling.libraryservice.dataPipe.libraryCatalog.step;


import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogAggregator;
import java.io.IOException;
import java.nio.file.Path;

public class AggregatingStep implements ExecutionStep{

    // "aggregatingStep\\aggregating"
    private final String outPutFileNm;

    public AggregatingStep(String outPutFileNm) {
        this.outPutFileNm = outPutFileNm;
    }

    @Override
    public Path execute(Path input) throws IOException {
        return LibraryCatalogAggregator.aggregateLoanCnt(
            input.subpath(0,2).toString(),
            outPutFileNm
        );
    }

}
