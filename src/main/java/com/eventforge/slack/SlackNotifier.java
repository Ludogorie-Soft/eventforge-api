package com.eventforge.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Component
public class SlackNotifier {
    private static final String webhookUrl = "https://hooks.slack.com/services/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";

    public void sendNotification(String message) {
        Payload payload = Payload.builder()
                .text(message)
                .build();

        try {
            Slack slack = Slack.getInstance();
            WebhookResponse response = slack.send(webhookUrl, payload);
            // You can handle the response if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

