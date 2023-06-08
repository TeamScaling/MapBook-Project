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

/**
 * {@link SlackTaskReporter} 클래스는 {@link TaskReporter} 인터페이스를 구현하고 있습니다.
 * 이 클래스는 작업 보고를 Slack 채널로 전달합니다. {@link AuthKeyLoader}를 통해 로드한 인증 키를 사용하여 Slack API에 액세스합니다.
 */
@Slf4j
@Component @RequiredArgsConstructor
public class SlackTaskReporter implements TaskReporter {

    private final AuthKeyLoader authKeyLoader;

    private String authKey;

    /**
     * 객체 생성 후 초기화 작업을 위한 메소드입니다.
     * {@link AuthKeyLoader}를 이용해 Slack Bot의 인증키를 로드합니다.
     */
    @PostConstruct
    private void loadKey(){
        authKey = authKeyLoader.loadAuthKey(OpenApi.SLACK_BOT).getAuthKey();
    }

    /**
     * Slack 채널로 작업 보고를 전달하는 메소드입니다.
     * 입력된 메시지를 형식에 맞게 변환 후, 지정된 Slack 채널로 메시지를 보냅니다.
     *
     * @param message 보고할 메시지
     */
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
