package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogNormalizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class NormalizeStep implements ExecutionStep {

    //"pipe/normalizeStep/normalFile.csv"
    private final String outPutFolderNm;


    public NormalizeStep(String outPutFolderNm) {
        this.outPutFolderNm = outPutFolderNm;
    }

    @Override
    public Path execute(Path input) throws IOException {
        return LibraryCatalogNormalizer.normalize(
            input.toString(),
            outPutFolderNm
        );
    }

}
