package com.scaling.libraryservice.commons.reporter;

import static org.junit.jupiter.api.Assertions.*;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlackTaskReporterTest {

    @InjectMocks
    private SlackTaskReporter slackTaskReporter;

    @Mock
    private MethodsClient methodsClient;

    @Test
    void report() throws SlackApiException, IOException {
        /* given */
        String msg = "hello";

        /* when */

        slackTaskReporter.report(msg);

        /* then */
    }

}