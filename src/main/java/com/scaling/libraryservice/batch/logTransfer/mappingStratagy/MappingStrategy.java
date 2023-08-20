package com.scaling.libraryservice.batch.logTransfer.mappingStratagy;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLogDto;
import com.scaling.libraryservice.batch.logTransfer.vo.SlackLogVo;

public interface MappingStrategy {
    SlackLogDto mapping(SlackLogVo slackLogVo);
}
