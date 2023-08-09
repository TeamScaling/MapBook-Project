package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import com.scaling.libraryservice.dataPipe.libraryCatalog.download.LibraryCatalogDownloader;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DownLoadStep implements ExecutionStep {

    private final LibraryCatalogDownloader libraryCatalogDownloader;
    private final String targetDate;

    private final boolean limitOption;
    private final int limit;

    @Override
    public Path execute(Path input) {
        if(targetDate == null){
            throw new NullPointerException();
        }
        return libraryCatalogDownloader.downLoad(input.toString(),targetDate,limitOption,limit);
    }

}
