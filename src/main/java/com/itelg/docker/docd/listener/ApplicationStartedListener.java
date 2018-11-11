package com.itelg.docker.docd.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.itelg.docker.docd.domain.ServiceSelectionMode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ApplicationReadyEvent>
{
    @Value("${service.selection.mode}")
    private ServiceSelectionMode mode;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg0)
    {
        log.info("Selection-Mode: " + mode);
    }
}