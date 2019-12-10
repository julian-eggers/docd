package com.itelg.docker.docd.listener;

public class RabbitMqWebHookEventListener extends AbstractWebHookEventListener
{
    public void onMessage(byte[] json)
    {
        handleMessage(new String(json));
    }
}
