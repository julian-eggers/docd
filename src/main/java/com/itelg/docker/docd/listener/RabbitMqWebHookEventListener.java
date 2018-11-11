package com.itelg.docker.docd.listener;

public class RabbitMqWebHookEventListener extends AbstractWebHookEventListener
{
    public void onMessage(byte[] json)
    {
        onMessage(webHookEventParser.parse(new String(json)));
    }
}