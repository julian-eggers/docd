package com.itelg.docker.docd.service;

import com.itelg.docker.docd.domain.WebHookEvent;

public interface WebHookEventService
{
    void processWebHookEvent(WebHookEvent event);
}