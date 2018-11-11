package com.itelg.docker.docd.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ServiceSpec;
import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.repository.ServiceRepository;
import com.itelg.docker.docd.util.ServiceUpdateUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DockerClientServiceRepository implements ServiceRepository
{
    private final DockerClient dockerClient;

    @Override
    public void recreate(Service service)
    {
        com.github.dockerjava.api.model.Service nativeService = getNativeServiceById(service.getId());
        ServiceSpec serviceSpec = nativeService.getSpec();

        Map<String, String> currentLabels = serviceSpec.getTaskTemplate().getContainerSpec().getLabels();
        serviceSpec.getTaskTemplate().getContainerSpec().withLabels(ServiceUpdateUtil.updateDeploymentLabels(currentLabels));

        String currentImage = serviceSpec.getTaskTemplate().getContainerSpec().getImage();
        serviceSpec.getTaskTemplate().getContainerSpec().withImage(normalizeImage(currentImage));

        dockerClient.updateServiceCmd(service.getId(), serviceSpec).withVersion(nativeService.getVersion().getIndex()).exec();
    }

    @Override
    public void updateLabels(Service service)
    {
        com.github.dockerjava.api.model.Service nativeService = getNativeServiceById(service.getId());
        ServiceSpec serviceSpec = nativeService.getSpec();
        serviceSpec.withLabels(service.getLabels());
        dockerClient.updateServiceCmd(service.getId(), serviceSpec).withVersion(nativeService.getVersion().getIndex()).exec();
    }

    private com.github.dockerjava.api.model.Service getNativeServiceById(String id)
    {
        return dockerClient.listServicesCmd().withIdFilter(Collections.singletonList(id)).exec().get(0);
    }

    @Override
    public void delete(Service service)
    {
        dockerClient.removeServiceCmd(service.getId()).exec();
    }

    @Override
    public Service getByName(String name)
    {
        com.github.dockerjava.api.model.Service nativeService = dockerClient.listServicesCmd().withNameFilter(Collections.singletonList(name)).exec().get(0);

        if (nativeService != null)
        {
            return convert(nativeService);
        }

        return null;
    }

    @Override
    public List<Service> getByImage(String image)
    {
        List<Service> matchingServices = new ArrayList<>();

        for (com.github.dockerjava.api.model.Service nativeService : dockerClient.listServicesCmd().exec())
        {
            String serviceImage = nativeService.getSpec().getTaskTemplate().getContainerSpec().getImage();

            if (normalizeImage(serviceImage).equals(image))
            {
                matchingServices.add(convert(nativeService));
            }
        }

        return matchingServices;
    }

    private static String normalizeImage(String image)
    {
        return image.split("@")[0];
    }

    private static Service convert(com.github.dockerjava.api.model.Service nativeService)
    {
        Service service = new Service();
        service.setId(nativeService.getId());
        service.setName(nativeService.getSpec().getName());
        service.setImage(nativeService.getSpec().getTaskTemplate().getContainerSpec().getImage());
        service.setLabels(new HashMap<>(nativeService.getSpec().getLabels()));

        return service;
    }
}
