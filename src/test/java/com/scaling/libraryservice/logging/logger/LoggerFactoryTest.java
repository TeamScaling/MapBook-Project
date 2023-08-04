package com.scaling.libraryservice.logging.logger;

import com.scaling.libraryservice.commons.circuitBreaker.ApiStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoggerFactoryTest {

    @Autowired
    LoggerFactory<ApiStatus> loggerFactory;


    @Test
    public void test(){
        /* given */

        ApiStatus apiStatus = new ApiStatus("test.com", 10);

        SlackLogger<ApiStatus> logger = loggerFactory.constructSlackLogger(TaskType.API_ERROR_TASK);

        /* when */

        logger.sendLogToSlack(apiStatus);

        /* then */
    }

}