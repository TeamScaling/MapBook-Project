package com.scaling.libraryservice.dataPipe.libraryCatalog.step;


import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogAggregator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AggregatingStep implements ExecutionStep{

    // "aggregatingStep\\aggregating"
    private final String outPutFileNm;
    private final int groupingSize;

    @Override
    public Path execute(Path input) throws IOException {
        log.info("[{}] start",this.getClass().getSimpleName());

        return LibraryCatalogAggregator.aggregateLoanCnt(
            input.subpath(0,2).toString(),
            outPutFileNm, groupingSize
        );
    }

}
