package com.itelg.docker.docd.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.parser.WebHookEventParser;
import com.itelg.docker.docd.service.WebHookEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractWebHookEventListener
{
    @Autowired
    private List<WebHookEventParser> webHookEventParsers;

    @Autowired
    private WebHookEventService updateService;

    protected void handleMessage(String json)
    {
        WebHookEvent webHookEvent = null;

        for (var webHookEventParser : webHookEventParsers)
        {
            try
            {
                webHookEvent = webHookEventParser.parse(json);
            }
            catch (@SuppressWarnings("unused") Exception e)
            {
                log.debug("Failed to parse with json with {} - {}", webHookEventParser.getClass().getSimpleName(), json);
            }
        }

        if (webHookEvent == null)
        {
            log.warn("Failed to parse with json - {}", json);
        }

        updateService.processWebHookEvent(webHookEvent);
    }
}
