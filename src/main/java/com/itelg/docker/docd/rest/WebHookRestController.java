package com.itelg.docker.docd.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.parser.DockerhubWebhookEventParser;
import com.itelg.docker.docd.parser.GithubWebhookEventParser;
import com.itelg.docker.docd.service.WebHookEventService;

@RestController
public class WebHookRestController
{
    @Lazy
    @Autowired
    private DockerhubWebhookEventParser dockerhubWebHookEventParser;

    @Lazy
    @Autowired
    private GithubWebhookEventParser githubWebhookEventParser;

    @Lazy
    @Autowired
    private WebHookEventService updateService;

    @PostMapping("webhook/dockerhub")
    public WebHookEvent processDockerhubWebHookEvent(@RequestBody String json)
    {
        var event = dockerhubWebHookEventParser.parse(json);
        updateService.processWebHookEvent(event);
        return event;
    }

    @PostMapping("webhook/github")
    public WebHookEvent processGithubWebHookEvent(@RequestBody String json)
    {
        var event = dockerhubWebHookEventParser.parse(json);
        updateService.processWebHookEvent(event);
        return event;
    }
}
