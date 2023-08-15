package com.scaling.libraryservice.batch.loanCnt.chunk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class DownLoadFileClearTask implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {
        Path root = Path.of("pipe");
        if (Files.exists(root) && Files.isDirectory(root)) {

            try (Stream<Path> pathStream = Files.walk(root)) {
                pathStream.filter(this::isCsvFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            }
        }
        return RepeatStatus.FINISHED;
    }

    private boolean isCsvFile(Path path) {
        return path.getFileName().toString().endsWith(".csv");
    }

}