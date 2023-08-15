package com.scaling.libraryservice.batch.loanCnt.util.download;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface DownLoader {

    void downLoad(String outPutDirectory, String targetDate,boolean option,int limit);
}
