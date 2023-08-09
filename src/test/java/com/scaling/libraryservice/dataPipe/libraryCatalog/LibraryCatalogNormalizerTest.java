package com.scaling.libraryservice.dataPipe.libraryCatalog;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.dataPipe.csv.util.CsvFileMerger;
import java.io.IOException;

class LibraryCatalogNormalizerTest {

    public static void main(String[] args) throws IOException {

//        LibraryCatalogNormalizer.normalize("pipe/download","pipe/normalizeStep");
//
//        CsvFileMerger merger = new CsvFileMerger();
//        CsvFileMerger.mergeCsvFile("pipe/normalizeStep","pipe/mergingStep/mergedFile.csv",0,1);
////
        LibraryCatalogAggregator.aggregateLoanCnt("pipe/normalizeStep","pipe/aggregatingStep/aggregate.csv",10);
//
//        CsvFileMerger.mergeCsvFile("pipe/aggregatingStep","pipe/mergingStep/merge.csv",0,1);

//        LibraryCatalogAggregator.aggregateLoanCnt("pipe/mergingStep","pipe/endStep/end.csv",10);

    }

}