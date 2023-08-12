package com.scaling.libraryservice.dataPipe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class FileService {

    public List<File> listFiles(String directory) {
        return Arrays.asList(Objects.requireNonNull(new File(directory).listFiles()));
    }

    public List<String> readAllLines(File file, Charset charset) throws IOException {
        return Files.readAllLines(file.toPath(), charset);
    }

    public BufferedWriter getBufferedWriter(String outputFilename, Charset charset,OpenOption option)
        throws IOException {
        return Files.newBufferedWriter(
            Paths.get(outputFilename),
            charset,
            StandardOpenOption.CREATE,
            option
        );
    }

    public void writeToCsv(String dataLine, String outPutNm, Charset charset,OpenOption option) {
        try (BufferedWriter writer = getBufferedWriter(outPutNm, charset,option)) {
            if (!dataLine.isBlank()){
                writer.write(dataLine);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<List<File>> groupFiles(List<File> files, int groupSize) {
        return IntStream.iterate(0, i -> i + groupSize)
            .limit((files.size() + groupSize - 1) / groupSize)
            .mapToObj(i -> files.subList(i, Math.min(i + groupSize, files.size())))
            .collect(Collectors.toList());
    }

    public File[] getCsvFiles(String folder) {
        return new File(folder).listFiles((dir, name) -> name.endsWith(".csv"));
    }

    public String join(List<String> lines) {
        return String.join(",", lines);
    }


}
