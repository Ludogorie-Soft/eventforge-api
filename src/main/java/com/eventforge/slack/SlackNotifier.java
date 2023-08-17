package com.eventforge.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Component
public class SlackNotifier {
    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/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";

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

