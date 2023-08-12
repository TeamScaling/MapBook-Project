package com.scaling.libraryservice.dataPipe.libraryCatalog.step;


import static java.nio.charset.StandardCharsets.UTF_8;

import com.scaling.libraryservice.dataPipe.FileService;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogAggregator;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AggregatingStep implements ExecutionStep {

    // "aggregatingStep\\aggregating"

    private final String inPutFolder;
    private final String outPutFileNm;
    private final int groupingSize;
    private final LibraryCatalogAggregator libraryCatalogAggregator;

    private final FileService fileService;

    @Override
    public Path execute(Path input) throws IOException {
        log.info("[{}] start", this.getClass().getSimpleName());

        List<File> filesList = fileService.listFiles(inPutFolder);

        fileService.groupFiles(filesList, groupingSize)
            .stream()
            .map(this::readLinesFromFiles)
            .map(libraryCatalogAggregator::aggregateLoanCnt)
            .flatMap(Collection::stream)
            .forEach(line -> fileService.writeToCsv(
                line,
                outPutFileNm,
                UTF_8,
                StandardOpenOption.APPEND
            ));

        return Path.of(outPutFileNm);
    }

    private List<String> readLinesFromFiles(List<File> fileList) {
        return fileList.parallelStream()
            .flatMap(file -> {
                try {
                    return fileService.readAllLines(file, UTF_8).stream();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })
            .collect(Collectors.toList());
    }

}
