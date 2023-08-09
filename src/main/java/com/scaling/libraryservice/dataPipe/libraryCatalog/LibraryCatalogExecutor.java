package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.dataPipe.libraryCatalog.download.LibraryCatalogDownloader;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.AggregatingStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.DownLoadStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.ExecutionStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.MergingStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.NormalizeStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.StepBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryCatalogExecutor {

    private final LibraryCatalogDownloader libraryCatalogDownloader;


    // targetDate "(2023년 06월)"
    @BatchLogging
    public void executeProcess(Path input, List<ExecutionStep> executionSteps,
        ExecutionStep... skipSteps) {

        AtomicReference<Path> currentPath = new AtomicReference<>(input);
        List<ExecutionStep> skipList = Arrays.stream(skipSteps).toList();

        List<Path> completePath = new ArrayList<>();

        executionSteps.stream()
            .filter(step -> !skipList.contains(step))
            .forEach(executionStep -> {
                try {
                    Path outPutPath = executionStep.execute(currentPath.get());

                    if (!currentPath.get().subpath(0, 2).toString().equals("pipe/endStep")) {
                        completePath.add(currentPath.get());
                    }

                    currentPath.set(outPutPath);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

        completePath.forEach(this::clearDirectory);
    }

    private void clearDirectory(Path filePath) {

        Path directory = filePath.subpath(0, 2);

        if (Files.exists(directory)) {

            try (Stream<Path> paths = Files.walk(directory)) {
                paths.filter(Files::isRegularFile) // 디렉토리는 제외하고 파일만 필터링
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    // targetDate =  "(2023년 06월)"
    public void defaultProcess(String targetDate) throws IOException {

        StepBuilder stepBuilder = new StepBuilder();
        List<ExecutionStep> executionSteps = stepBuilder
            .start(new DownLoadStep(libraryCatalogDownloader, targetDate, false, -1))
            .next(new NormalizeStep("pipe/normalizedStep/"))
            .next(new AggregatingStep("pipe/aggregatingStep/aggregating", 100))
            .next(new MergingStep("pipe/mergingStep/mergeFile.csv"))
            .next(new AggregatingStep("pipe/endStep/end", 10))
            .end();

        executeProcess(Path.of("pipe/download/"), executionSteps);
    }
}
