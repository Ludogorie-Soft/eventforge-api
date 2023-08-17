package com.eventforge.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Component
public class SlackNotifier {
    @Value("${webhook.url}")
    private  String WEBHOOK_URL;

    public void sendNotification(String message) {
        Payload payload = Payload.builder()
                .text(message)
                .build();

        try {
            Slack slack = Slack.getInstance();
            slack.send(WEBHOOK_URL, payload);
            // You can handle the response if needed
        } catch (IOException ignored) {
        }
    }
}

