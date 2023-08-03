package com.scaling.libraryservice.dataPipe.download;

import java.nio.file.Path;

public interface DownLoader {

    Path downLoad(String outPutDirectory, String targetDate);
}
