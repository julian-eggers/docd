package com.itelg.docker.docd.listener;

import org.springframework.beans.factory.annotation.Autowired;

import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.parser.DockerhubWebhookEventParser;
import com.itelg.docker.docd.service.WebHookEventService;

public abstract class AbstractWebHookEventListener
{
    @Autowired
    protected DockerhubWebhookEventParser webHookEventParser;

    @Autowired
    protected WebHookEventService updateService;

    protected void onMessage(WebHookEvent webHookEvent)
    {
        updateService.processWebHookEvent(webHookEvent);
    }
}
