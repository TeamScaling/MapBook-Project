package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLogDto;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;
import com.scaling.libraryservice.logging.util.parser.SlackLogParser;
import java.util.function.Function;

public class MapBookMapping extends AbstractMapping implements MappingStrategy{

    public MapBookMapping(SlackLogParser slackLogParser) {
        super(slackLogParser);
    }

    @Override
    public SlackLogDto mapping(SlackLogVo slackLogVo) {
        return SlackLogDto.builder()
            .taskCode(formatTaskType(slackLogVo))
            .title(formatTitle(slackLogVo))
            .areaCd(formatAreaCd(slackLogVo))
            .logDateTime(formatDateTime(slackLogVo))
            .taskTime(formatTaskTime(slackLogVo,"taskTime"))
            .build();
    }

    private Integer formatAreaCd(SlackLogVo slackLogVo) {
        return extractFromLog(slackLogVo, "areaCd", Integer::parseInt, 0);
    }

    private String formatTitle(SlackLogVo slackLogVo) {
        return extractFromLog(slackLogVo, "title", Function.identity(), "");
    }
}
