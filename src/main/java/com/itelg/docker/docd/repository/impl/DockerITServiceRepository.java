package com.itelg.docker.docd.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.repository.ServiceRepository;
import com.itelg.docker.docd.util.ServiceUpdateUtil;

public class DockerITServiceRepository implements ServiceRepository
{
    private Set<Service> services = new HashSet<>();

    public void add(Service service)
    {
        services.add(service);
    }

    public void deleteAll()
    {
        services = new HashSet<>();
    }

    @Override
    public void recreate(Service service)
    {
        service.setLabels(ServiceUpdateUtil.updateDeploymentLabels(service.getLabels()));
    }

    @Override
    public void updateLabels(Service service)
    {

    }

    @Override
    public void delete(Service service)
    {
        services.remove(service);
    }

    @Override
    public Service getByName(String name)
    {
        return services.stream().filter(match -> match.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<Service> getByImage(String image)
    {
        return services.stream().filter(match -> normalizeImage(match.getImage()).equals(image)).collect(toList());
    }

    private static String normalizeImage(String image)
    {
        return image.split("@")[0];
    }
}