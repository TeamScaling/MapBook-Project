package com.scaling.libraryservice.batch.logTransfer.chunk;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLog;
import com.scaling.libraryservice.batch.logTransfer.entity.SlackLogDto;
import com.scaling.libraryservice.batch.logTransfer.entity.Task;
import com.scaling.libraryservice.batch.logTransfer.mappingStratagy.MappingStrategyFactory;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import com.scaling.libraryservice.logging.repository.TaskRepository;
import com.scaling.libraryservice.logging.util.parser.SlackLogParser;
import com.scaling.libraryservice.mapBook.entity.LibraryMeta;
import com.scaling.libraryservice.mapBook.repository.LibraryMetaRepository;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LogItemProcessor implements ItemProcessor<SlackLogVo, SlackLog> {

    private final TaskRepository taskRepository;
    private final LibraryMetaRepository libraryMetaRepository;
    private final MappingStrategyFactory mappingStrategyFactory;
    private Map<Integer, Task> taskMap;
    private Map<Integer, LibraryMeta> libraryMetaMap;
    private final SlackLogParser slackLogParser;

    @PostConstruct
    private void setUp() {
        this.taskMap = setUpTaskMap();
        this.libraryMetaMap = setUpLibraryMetaMap();
    }

    @Nullable
    @Override
    public SlackLog process(SlackLogVo item) throws Exception {

        Optional<String> optionalTaskCode = slackLogParser.extractMessageFromLog(
            item,
            slackLogParser.getTaskCodeRegex()
        );

        if (optionalTaskCode.isEmpty()) {
            return null;
        }

        Integer taskCode = Integer.parseInt(optionalTaskCode.get());
        SlackLogDto slackLogDto = mappingStrategyFactory.getMappingStrategy(taskCode)
            .mapping(item);

        return mapToSlackLog(slackLogDto);
    }


    private SlackLog mapToSlackLog(SlackLogDto slackLogDto) {

        Task task = taskMap.get(slackLogDto.getTaskCode());
        LibraryMeta libraryMeta = libraryMetaMap.get(slackLogDto.getAreaCd());
        return new SlackLog(task, libraryMeta, slackLogDto);
    }

    private Map<Integer, Task> setUpTaskMap() {
        return taskRepository.findAll().stream()
            .collect(Collectors.toMap(Task::getCode, Function.identity()));
    }

    private Map<Integer, LibraryMeta> setUpLibraryMetaMap() {
        return libraryMetaRepository.findAll().stream()
            .collect(Collectors.toMap(LibraryMeta::getAreaCd, Function.identity()));
    }

    public void clear() {
        this.taskMap.clear();
        this.libraryMetaMap.clear();
    }
}
