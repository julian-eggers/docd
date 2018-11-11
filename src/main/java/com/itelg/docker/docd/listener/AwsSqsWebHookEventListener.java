package com.itelg.docker.docd.listener;

import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;

public class AwsSqsWebHookEventListener extends AbstractWebHookEventListener
{
    @SqsListener(value = "${webhookevent.queue.awssqs.queue-name}")
    public void onMessage(String json)
    {
        onMessage(webHookEventParser.parse(json));
    }
}
