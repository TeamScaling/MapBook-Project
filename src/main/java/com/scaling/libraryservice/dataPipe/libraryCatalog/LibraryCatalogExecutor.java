package com.scaling.libraryservice.dataPipe.libraryCatalog;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.dataPipe.download.LibraryCatalogDownloader;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.AggregatingStep;
import com.scaling.libraryservice.dataPipe.libraryCatalog.step.DivideStep;
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
import org.springframework.util.StopWatch;

@RequiredArgsConstructor
@Service
public class LibraryCatalogExecutor {

    private final LibraryCatalogDownloader libraryCatalogDownloader;

//    private final NormalizeStep normalizeStep;

    // targetDate "(2023년 06월)"
    @BatchLogging
    public void executeProcess(Path input, List<ExecutionStep> executionSteps,
        ExecutionStep... skipStep) {

        AtomicReference<Path> currentPath = new AtomicReference<>(input);
        List<ExecutionStep> skipStepList = Arrays.stream(skipStep).toList();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Path> completePath = new ArrayList<>();

        executionSteps.stream()
            .filter(step -> !skipStepList.contains(step))
            .forEach(executionStep -> {
                try {
                    Path outPutPath = executionStep.execute(currentPath.get());

                    if(!(executionStep instanceof MergingStep)){
                        completePath.add(currentPath.get());
                    }

                    currentPath.set(outPutPath);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

        stopWatch.stop();
        String totalTime = String.format("%.3f", stopWatch.getTotalTimeSeconds());

        System.out.println("totalTime : "+totalTime);

        completePath.forEach(this::clearDirectory);
    }

    private void clearDirectory(Path filePath){

        Path directory = filePath.subpath(0, 2);

        System.out.println(directory);

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
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }
        }
    }

//    // targetDate =  "(2023년 06월)"
//    public void defaultProcess(String targetDate) throws IOException {
//
//        StepBuilder stepBuilder = new StepBuilder();
//        List<ExecutionStep> executionSteps = stepBuilder
//            .start(new DownLoadStep(libraryCatalogDownloader, targetDate, false, -1))
//            .next(normalizeStep)
//            .next(new DivideStep("pipe/divideStep", 10000000))
//            .next(new AggregatingStep("pipe/aggregatingStep/aggregating"))
//            .next(new MergingStep("pipe/mergingStep/mergeFile.csv"))
//            .next(new AggregatingStep("pipe/endStep/end"))
//            .end();
//
//        executeProcess(Path.of("pipe/download/"), executionSteps);
//    }
}
