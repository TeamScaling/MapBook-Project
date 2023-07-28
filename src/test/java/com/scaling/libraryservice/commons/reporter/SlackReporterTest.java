package com.scaling.libraryservice.commons.reporter;

import com.scaling.libraryservice.logging.util.SlackReporter;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlackReporterTest {

    @InjectMocks
    private SlackReporter slackReporter;

    @Mock
    private MethodsClient methodsClient;

    @Test
    void report() throws SlackApiException, IOException {
        /* given */
        String msg = "hello";

        /* when */

        slackReporter.report(msg,"#mapbook");

        /* then */
    }

}