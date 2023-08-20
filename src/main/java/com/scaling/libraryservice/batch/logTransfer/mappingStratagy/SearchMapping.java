package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLogDto;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import com.scaling.libraryservice.logging.util.parser.SlackLogParser;
import java.util.function.Function;

public class SearchMapping extends AbstractMapping implements MappingStrategy {

    public SearchMapping(SlackLogParser slackLogParser) {
        super(slackLogParser);
    }

    @Override
    public SlackLogDto mapping(SlackLogVo slackLogVo) {
        return SlackLogDto.builder()
            .logDateTime(formatDateTime(slackLogVo))
            .title(formatUserQuery(slackLogVo))
            .taskCode(formatTaskType(slackLogVo))
            .taskTime(formatTaskTime(slackLogVo, "searchTime"))
            .areaCd(null)
            .build();
    }

    private String formatUserQuery(SlackLogVo slackLogVo) {
        return extractFromLog(
            slackLogVo,
            "userQuery",
            Function.identity(),
            ""
        );
    }


}
