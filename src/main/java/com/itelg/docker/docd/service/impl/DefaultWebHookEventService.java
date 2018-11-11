package com.itelg.docker.docd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.service.ServiceService;
import com.itelg.docker.docd.service.WebHookEventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultWebHookEventService implements WebHookEventService
{
    @Autowired
    private ServiceService serviceService;

    @Override
    public void processWebHookEvent(WebHookEvent event)
    {
        log.info("Processing " + event);

        for (com.itelg.docker.docd.domain.Service service : serviceService.getByImage(event.getImage()))
        {
            serviceService.redeploy(service);
        }
    }
}