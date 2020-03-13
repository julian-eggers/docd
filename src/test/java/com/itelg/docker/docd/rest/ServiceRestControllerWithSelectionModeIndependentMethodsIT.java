package com.itelg.docker.docd.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

import com.itelg.docker.docd.domain.Service;

public class ServiceRestControllerWithSelectionModeIndependentMethodsIT extends AbstractRestIT
{
    @Test
    public void testAllowByNameWithNotFound()
    {
        allowWithNotFound("notfound");
    }

    @Test
    public void testBlockByNameWithNotFound()
    {
        blockWithNotFound("notfound");
    }

    @Test
    public void testRedeployByNameWithNotFound()
    {
        redeployWithNotFound("notfound");
    }

    @Test
    public void testRedeployByNameWithFirstRedeployment()
    {
        // Preconditions
        repository.add(getBaseService());

        // Action
        Service result = redeploy("cawandu");

        // Result check
        assertEquals("1", result.getLabelByKey(Service.DEPLOYMENT_COUNT_LABEL));
        assertNotNull(result.getLabelByKey(Service.LAST_DEPLOYMENT_LABEL));
    }

    @Test
    public void testRedeployByNameWithPreviousRedeployment()
    {
        // Preconditions
        repository.add(getServiceWithAlreadyDeployed());

        // Action
        Service result = redeploy("cawandu");

        // Result check
        assertEquals("2", result.getLabelByKey(Service.DEPLOYMENT_COUNT_LABEL));
        assertNotNull(result.getLabelByKey(Service.LAST_DEPLOYMENT_LABEL));
    }

    @Test
    public void testDeleteByNameWithNotFound()
    {
        // Action
        delete("notfound");
    }

    @Test
    public void testDeleteByName()
    {
        // Preconditions
        repository.add(getBaseService());

        // Action
        delete("cawandu");

        // Database check
        assertNull(repository.getByName("cawandu"));
    }

    @Test
    public void testGetByNameWithNotFound()
    {
        getWithNotFound("notfound");
    }

    @Test
    public void testGetByName()
    {
        // Preconditions
        repository.add(getBaseService());

        // Action
        assertNotNull(get("cawandu"));
    }
}
