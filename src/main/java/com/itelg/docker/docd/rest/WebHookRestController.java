package com.itelg.docker.docd.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.parser.DockerhubWebhookEventParser;
import com.itelg.docker.docd.service.WebHookEventService;

@RestController
public class WebHookRestController
{
    @Lazy
    @Autowired
    private DockerhubWebhookEventParser webHookEventParser;

    @Lazy
    @Autowired
    private WebHookEventService updateService;

    @PostMapping("webhook/dockerhub")
    public WebHookEvent processDockerhubWebHookEvent(@RequestBody String json)
    {
        WebHookEvent event = webHookEventParser.parse(json);
        updateService.processWebHookEvent(event);
        return event;
    }
}