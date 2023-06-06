package com.scaling.libraryservice.commons.reporter;

import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.service.AuthKeyLoader;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component @RequiredArgsConstructor
public class SlackTaskReporter implements TaskReporter {

    private final AuthKeyLoader authKeyLoader;

    private String authKey;

    @PostConstruct
    private void loadKey(){
        authKey = authKeyLoader.loadAuthKey(OpenApi.SLACK_BOT).getAuthKey();
    }

    @Override
    public void report(String message) {

        message = String.format("[%s] Search task is over 3s times. Please Check this",message);

        MethodsClient methods = Slack.getInstance().methods(authKey);

        String channelAddress = "#mapbook";
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .channel(channelAddress)
            .text(message)
            .build();

        try {
            methods.chatPostMessage(request);
        } catch (IOException | SlackApiException e) {
            log.info(e.getMessage());
        }
    }
}
