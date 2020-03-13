package com.itelg.docker.docd.rest;

import static com.itelg.docker.docd.domain.Service.DEPLOYMENT_COUNT_LABEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.test.support.AbstractIT;

@TestPropertySource(properties = "service.selection.mode=WHITELIST")
public class WebHookRestControllerWithWhitelistModeIT extends AbstractIT
{
    @Autowired
    private WebHookRestController controller;

    @Test
    public void testProcessWebHookEventWithNotAllowed()
    {
        // Preconditions
        repository.add(getBaseService());

        // Action
        WebHookEvent webHookEvent = controller.processDockerhubWebHookEvent(getDockerHubWebHookEventAsJson());

        // Result check
        assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());

        // Database check
        Service redeployed = repository.getByName("cawandu");
        assertFalse(redeployed.hasLabel(DEPLOYMENT_COUNT_LABEL));
    }

    @Test
    public void testProcessWebHookEvent()
    {
        // Preconditions
        repository.add(getBaseService().whitelisted());

        // Action
        WebHookEvent webHookEvent = controller.processDockerhubWebHookEvent(getDockerHubWebHookEventAsJson());

        // Result check
        assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());

        // Database check
        Service redeployed = repository.getByName("cawandu");
        assertEquals("1", redeployed.getLabelByKey(DEPLOYMENT_COUNT_LABEL));
    }
}
