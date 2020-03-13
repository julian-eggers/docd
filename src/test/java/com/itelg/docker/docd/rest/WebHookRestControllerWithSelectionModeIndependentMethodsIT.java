package com.itelg.docker.docd.rest;

import static com.itelg.docker.docd.domain.Service.DEPLOYMENT_COUNT_LABEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.test.support.AbstractIT;

@TestPropertySource(properties = "service.selection.mode=BLACKLIST")
public class WebHookRestControllerWithSelectionModeIndependentMethodsIT extends AbstractIT
{
    @Autowired
    private WebHookRestController controller;

    @Nested
    public class DockerhubTests
    {
        @Test
        public void testProcessWebHookEventWithNotFound()
        {
            // Action
            WebHookEvent webHookEvent = controller.processDockerhubWebHookEvent(getDockerHubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());
        }

        @Test
        public void testProcessWebHookEventWithImageHash()
        {
            // Preconditions
            Service service = getBaseService();
            service.setImage("jeggers/cawandu:latest@sha256:c4ffb730d54baf3ff135de96eb4393796e51f9ceb2f7592b4bd3a842428b39d1");
            repository.add(service);

            // Action
            WebHookEvent webHookEvent = controller.processDockerhubWebHookEvent(getDockerHubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());

            // Database check
            Service redeployed = repository.getByName("cawandu");
            assertEquals("1", redeployed.getLabelByKey(DEPLOYMENT_COUNT_LABEL));
        }

        @Test
        public void testProcessWebHookEventWithWrongTag()
        {
            // Preconditions
            Service service = getBaseService();
            service.setImage("jeggers/cawandu:1.0.0-RELEASE");
            repository.add(service);

            // Action
            WebHookEvent webHookEvent = controller.processDockerhubWebHookEvent(getDockerHubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());

            // Database check
            Service redeployed = repository.getByName("cawandu");
            assertFalse(redeployed.hasLabel(DEPLOYMENT_COUNT_LABEL));
        }

        @Test
        public void testProcessWebHookEventWithMultipleWrongTags()
        {
            // Preconditions
            Service service1 = getBaseService("cawandu10");
            service1.setImage("jeggers/cawandu:1.0.0-RELEASE");
            repository.add(service1);

            Service service2 = getBaseService("cawandu11");
            service2.setImage("jeggers/cawandu:1.1.0-RELEASE");
            repository.add(service2);

            // Action
            WebHookEvent webHookEvent = controller.processDockerhubWebHookEvent(getDockerHubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getBaseWebHookEvent());

            // Database check
            Service redeployed1 = repository.getByName("cawandu10");
            assertFalse(redeployed1.hasLabel(DEPLOYMENT_COUNT_LABEL));

            Service redeployed2 = repository.getByName("cawandu11");
            assertFalse(redeployed2.hasLabel(DEPLOYMENT_COUNT_LABEL));
        }
    }

    @Nested
    public class GithubTests
    {
        @Test
        public void testProcessWebHookEventWithNotFound()
        {
            // Action
            WebHookEvent webHookEvent = controller.processGithubWebHookEvent(getGithubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getGithubWebHookEvent());
        }

        @Test
        public void testProcessWebHookEventWithImageHash()
        {
            // Preconditions
            Service service = getBaseService();
            service.setImage("docker.pkg.github.com/jeggers/cawandu:latest@sha256:c4ffb730d54baf3ff135de96eb4393796e51f9ceb2f7592b4bd3a842428b39d1");
            repository.add(service);

            // Action
            WebHookEvent webHookEvent = controller.processGithubWebHookEvent(getGithubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getGithubWebHookEvent());

            // Database check
            Service redeployed = repository.getByName("cawandu");
            assertEquals("1", redeployed.getLabelByKey(DEPLOYMENT_COUNT_LABEL));
        }

        @Test
        public void testProcessWebHookEventWithWrongTag()
        {
            // Preconditions
            Service service = getBaseService();
            service.setImage("jeggers/cawandu:1.0.0-RELEASE");
            repository.add(service);

            // Action
            WebHookEvent webHookEvent = controller.processGithubWebHookEvent(getGithubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getGithubWebHookEvent());

            // Database check
            Service redeployed = repository.getByName("cawandu");
            assertFalse(redeployed.hasLabel(DEPLOYMENT_COUNT_LABEL));
        }

        @Test
        public void testProcessWebHookEventWithMultipleWrongTags()
        {
            // Preconditions
            Service service1 = getBaseService("cawandu10");
            service1.setImage("jeggers/cawandu:1.0.0-RELEASE");
            repository.add(service1);

            Service service2 = getBaseService("cawandu11");
            service2.setImage("jeggers/cawandu:1.1.0-RELEASE");
            repository.add(service2);

            // Action
            WebHookEvent webHookEvent = controller.processGithubWebHookEvent(getGithubWebHookEventAsJson());

            // Result check
            assertThat(webHookEvent).isEqualToComparingFieldByFieldRecursively(getGithubWebHookEvent());

            // Database check
            Service redeployed1 = repository.getByName("cawandu10");
            assertFalse(redeployed1.hasLabel(DEPLOYMENT_COUNT_LABEL));

            Service redeployed2 = repository.getByName("cawandu11");
            assertFalse(redeployed2.hasLabel(DEPLOYMENT_COUNT_LABEL));
        }
    }
}
