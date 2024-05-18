package com.scaling.libraryservice.logging.util;

import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.service.AuthKeyLoader;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 이 클래스는 작업 보고를 Slack 채널로
 * 전달합니다. {@link AuthKeyLoader}를 통해 로드한 인증 키를 사용하여 Slack API에 액세스합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SlackReporter {

    private final AuthKeyLoader authKeyLoader;

    private String authKey;

    /**
     * 객체 생성 후 초기화 작업을 위한 메소드입니다. {@link AuthKeyLoader}를 이용해 Slack Bot의 인증키를 로드합니다.
     */
    @PostConstruct
    private void loadKey() {
        authKey = authKeyLoader.loadAuthKey(OpenApi.SLACK_BOT).getAuthKey();
    }

    public void report(String message, String channelAddress) {
        ChatPostMessageRequest request = buildChatRequest(message, channelAddress);
        try {
            Slack.getInstance().methods(authKey).chatPostMessage(request);
        } catch (IOException | SlackApiException e) {
            log.info(e.getMessage());
        }
    }

    private ChatPostMessageRequest buildChatRequest(String message, String channelAddress) {
        return ChatPostMessageRequest.builder()
            .channel(channelAddress)
            .text(message)
            .build();
    }
}
