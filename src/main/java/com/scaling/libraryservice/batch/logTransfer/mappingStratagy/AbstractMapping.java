package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLogDto;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import com.scaling.libraryservice.logging.util.parser.SlackLogParser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractMapping implements MappingStrategy {

    private final SlackLogParser slackLogParser;

    public abstract SlackLogDto mapping(SlackLogVo slackLogVo);

    // 메소드 추상화를 위한 제네릭 메소드
    protected <T> T extractFromLog(SlackLogVo slackLogVo, String target,
        Function<String, T> converter, T defaultValue) {

        String detailCommonRegex = slackLogParser.getDetailCommonRegex(target);
        Optional<String> optional = slackLogParser.extractMessageFromLog(slackLogVo, detailCommonRegex);

        return optional.map(converter).orElse(defaultValue);
    }

    protected int formatTaskType(SlackLogVo slackLogVo) {
        return extractFromLog(slackLogVo, slackLogParser.getTaskCodeRegex(), Integer::parseInt, 0);
    }

    protected Double formatTaskTime(SlackLogVo slackLogVo, String target) {
        return extractFromLog(slackLogVo, slackLogParser.getDetailCommonRegex(target),
            Double::parseDouble, 0.0);
    }

    protected LocalDateTime formatDateTime(SlackLogVo slackLogVo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return extractFromLog(slackLogVo, slackLogParser.getDateTimeRegex(),
            s -> LocalDateTime.parse(s, formatter), null);
    }
}

