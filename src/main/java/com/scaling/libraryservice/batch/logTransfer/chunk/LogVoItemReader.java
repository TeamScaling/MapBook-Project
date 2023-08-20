package com.scaling.libraryservice.batch.logTransfer.chunk;

import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
public class LogVoItemReader implements ResourceAwareItemReaderItemStream<SlackLogVo> {

    private Resource resource;

    private final JsonItemReader<SlackLogVo> jsonItemReader;

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        jsonItemReader.setResource(resource);
    }

    @Nullable
    @Override
    public SlackLogVo read()
        throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return jsonItemReader.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        jsonItemReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        jsonItemReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        jsonItemReader.close();
    }

}
