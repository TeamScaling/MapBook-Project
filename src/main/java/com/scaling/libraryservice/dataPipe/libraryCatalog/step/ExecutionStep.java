package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ExecutionStep {

    Path execute(Path input) throws IOException;
}
