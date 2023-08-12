package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.scaling.libraryservice.dataPipe.FileService;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogNormalizer;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NormalizeStep implements ExecutionStep {

    //"pipe/normalizeStep/normalFile.csv"
    private final String outPutFolderNm;

    private final LibraryCatalogNormalizer libraryCatalogNormalizer;

    private final FileService fileService;


    @Override
    public Path execute(Path input) throws IOException {
        File[] csvFiles = fileService.getCsvFiles(input.toString());

        Arrays.stream(csvFiles).parallel()
            .forEach(this::processFile);

        return Path.of(outPutFolderNm);
    }

    private void processFile(File file) {
        try {
            List<String> lines = fileService.readAllLines(file, Charset.forName("EUC-KR"));
            List<String> normalizedLines = libraryCatalogNormalizer.normalize(lines);

            String outputFileName = generateOutputFileName(file.getName());
            fileService.writeToCsv(String.join("\n", normalizedLines), outputFileName, UTF_8,
                StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String generateOutputFileName(String originalFileName) {
        return Paths.get(outPutFolderNm, originalFileName.replace(".csv", "normalized.csv"))
            .toString();
    }


}
