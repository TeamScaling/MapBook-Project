package com.scaling.libraryservice.logging;

import com.scaling.libraryservice.logging.logger.TaskType;
import com.scaling.libraryservice.logging.parser.SlackLogParser;

class LogParserTest {

    public void parsingLogData() {
        /* given */

        var result = SlackLogParser.parsingLogData("C:\\teamScaling\\error 분석",
            TaskType.NOTFOUND_TASK,
            SlackLogParser.getUserQueryRegex());

        /* when */
        System.out.println(result);
        /* then */
    }

}