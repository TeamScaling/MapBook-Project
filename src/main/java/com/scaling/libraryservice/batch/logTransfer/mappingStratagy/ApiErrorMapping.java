package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLogDto;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import com.scaling.libraryservice.logging.util.parser.SlackLogParser;
import java.util.function.Function;

public class ApiErrorMapping extends AbstractMapping implements MappingStrategy {

    public ApiErrorMapping(SlackLogParser slackLogParser) {
        super(slackLogParser);
    }

    @Override
    public SlackLogDto mapping(SlackLogVo slackLogVo) {
        return SlackLogDto.builder()
            .logDateTime(formatDateTime(slackLogVo))
            .apiUrl(formatApiUrl(slackLogVo))
            .taskCode(formatTaskType(slackLogVo))
            .areaCd(null)
            .build();
    }

    private String formatApiUrl(SlackLogVo slackLogVo) {
        return extractFromLog(
            slackLogVo,
            "apiUrl",
            Function.identity(),
            ""
        );
    }

}
