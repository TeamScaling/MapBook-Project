package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ExecutionStep {

    Path execute(Path input) throws IOException;

    default void clearDirectory(Path filePath) throws IOException {

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
            }
        }
    }
}
