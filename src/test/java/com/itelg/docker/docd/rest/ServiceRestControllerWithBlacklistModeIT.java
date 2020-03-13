package com.itelg.docker.docd.rest;

import static com.itelg.docker.docd.domain.ServiceSelectionMode.BLACKLIST_BLOCKED_LABEL;
import static com.itelg.docker.docd.domain.ServiceSelectionMode.WHITELIST_ALLOWED_LABEL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import com.itelg.docker.docd.domain.Service;

@TestPropertySource(properties = "service.selection.mode=BLACKLIST")
public class ServiceRestControllerWithBlacklistModeIT extends AbstractRestIT
{
    @Test
    public void testAllowByNameWithAlreadyAllowed()
    {
        // Preconditions
        repository.add(getBaseService().whitelisted());

        // Action
        Service result = allow("cawandu");

        // Result check
        assertEquals(1, result.getLabels().size());
        assertTrue(result.hasLabel(WHITELIST_ALLOWED_LABEL));
    }

    @Test
    public void testAllowByNameWithCurrentlyBlocked()
    {
        // Preconditions
        repository.add(getBaseService().blacklisted());

        // Action
        Service result = allow("cawandu");

        // Result check
        assertEquals(0, result.getLabels().size());
        assertFalse(result.hasLabel(BLACKLIST_BLOCKED_LABEL));
    }

    @Test
    public void testAllowByName()
    {
        // Preconditions
        repository.add(getBaseService());

        // Action
        Service result = allow("cawandu");

        // Result check
        assertEquals(0, result.getLabels().size());
        assertFalse(result.hasLabel(WHITELIST_ALLOWED_LABEL));
    }

    @Test
    public void testBlockByNameWithAlreadyBlocked()
    {
        // Preconditions
        repository.add(getBaseService().blacklisted());

        // Action
        Service result = block("cawandu");

        // Result check
        assertEquals(1, result.getLabels().size());
        assertTrue(result.hasLabel(BLACKLIST_BLOCKED_LABEL));
    }

    @Test
    public void testBlockByName()
    {
        // Preconditions
        repository.add(getBaseService());

        // Action
        Service result = block("cawandu");

        // Result check
        assertEquals(1, result.getLabels().size());
        assertTrue(result.hasLabel(BLACKLIST_BLOCKED_LABEL));
    }

    @Test
    public void testRedeployByNameWithBlacklistAllowed()
    {
        // Preconditions
        repository.add(getServiceWithAlreadyDeployed());

        // Action
        Service result = redeploy("cawandu");

        // Result check
        assertEquals(2, result.getLabels().size());
        assertEquals("2", result.getLabelByKey(Service.DEPLOYMENT_COUNT_LABEL));
        assertNotNull(result.getLabelByKey(Service.LAST_DEPLOYMENT_LABEL));
    }

    @Test
    public void testRedeployByNameWithBlacklistBlocked()
    {
        // Preconditions
        repository.add(getServiceWithAlreadyDeployed().blacklisted());

        // Action
        Service result = redeploy("cawandu");

        // Result check
        assertEquals(2, result.getLabels().size());
        assertEquals("1", result.getLabelByKey(Service.DEPLOYMENT_COUNT_LABEL));
    }
}
