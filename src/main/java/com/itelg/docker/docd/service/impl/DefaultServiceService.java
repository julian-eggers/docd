package com.itelg.docker.docd.service.impl;

import static com.itelg.docker.docd.domain.ServiceSelectionMode.BLACKLIST;
import static com.itelg.docker.docd.domain.ServiceSelectionMode.BLACKLIST_BLOCKED_LABEL;
import static com.itelg.docker.docd.domain.ServiceSelectionMode.WHITELIST;
import static com.itelg.docker.docd.domain.ServiceSelectionMode.WHITELIST_ALLOWED_LABEL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.domain.ServiceSelectionMode;
import com.itelg.docker.docd.repository.ServiceRepository;
import com.itelg.docker.docd.service.ServiceService;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class DefaultServiceService implements ServiceService
{
    @Value("${service.selection.mode}")
    private ServiceSelectionMode mode;

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public void allowDeployment(Service service)
    {
        if (mode == WHITELIST && !service.hasLabel(WHITELIST_ALLOWED_LABEL))
        {
            log.info("Enabling redeploy for " + service);
            service.addLabel(WHITELIST_ALLOWED_LABEL, "true");
            updateLabels(service);
        }
        else if (mode == BLACKLIST && service.hasLabel(BLACKLIST_BLOCKED_LABEL))
        {
            log.info("Enabling redeploy for " + service);
            service.removeLabel(BLACKLIST_BLOCKED_LABEL);
            updateLabels(service);
        }
    }

    @Override
    public void blockDeployment(Service service)
    {
        if (mode == WHITELIST && service.hasLabel(WHITELIST_ALLOWED_LABEL))
        {
            log.info("Disabling redeploy for " + service);
            service.removeLabel(WHITELIST_ALLOWED_LABEL);
            updateLabels(service);
        }
        else if (mode == BLACKLIST && !service.hasLabel(BLACKLIST_BLOCKED_LABEL))
        {
            log.info("Disabling redeploy for " + service);
            service.addLabel(BLACKLIST_BLOCKED_LABEL, "true");
            updateLabels(service);
        }
    }

    @Override
    public void redeploy(Service service)
    {
        if (isAllowedToDeploy(mode, service))
        {
            log.info("Redeploying " + service);
            serviceRepository.recreate(service);
        }
        else
        {
            log.info("Service not allowed to redeploy (" + service + ")");
        }
    }

    @Override
    public void updateLabels(Service service)
    {
        log.info("Updating labels for " + service);
        serviceRepository.updateLabels(service);
    }

    @Override
    public void delete(Service service)
    {
        log.info("Deleting " + service);
        serviceRepository.delete(service);
    }

    @Override
    public Service getByName(String name)
    {
        return serviceRepository.getByName(name);
    }

    @Override
    public List<Service> getByImage(String image)
    {
        return serviceRepository.getByImage(image);
    }

    private static boolean isAllowedToDeploy(ServiceSelectionMode mode, Service service)
    {
        if (mode == WHITELIST)
        {
            return service.hasLabel(WHITELIST_ALLOWED_LABEL);
        }

        return !service.hasLabel(BLACKLIST_BLOCKED_LABEL);
    }
}
