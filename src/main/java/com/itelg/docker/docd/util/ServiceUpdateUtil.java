package com.itelg.docker.docd.util;

import static com.itelg.docker.docd.domain.Service.DEPLOYMENT_COUNT_LABEL;
import static com.itelg.docker.docd.domain.Service.LAST_DEPLOYMENT_LABEL;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceUpdateUtil
{
    public Map<String, String> updateDeploymentLabels(Map<String, String> currentLabels)
    {
        Map<String, String> updatedLabels = currentLabels != null ? new HashMap<>(currentLabels) : new HashMap<>();

        int currentDeploymentCount = Integer.parseInt(updatedLabels.getOrDefault(DEPLOYMENT_COUNT_LABEL, "0"));
        updatedLabels.put(DEPLOYMENT_COUNT_LABEL, String.valueOf(++currentDeploymentCount));

        updatedLabels.put(LAST_DEPLOYMENT_LABEL, LocalDateTime.now().toString());

        return updatedLabels;
    }
}