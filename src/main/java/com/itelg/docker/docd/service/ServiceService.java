package com.itelg.docker.docd.service;

import java.util.List;

import com.itelg.docker.docd.domain.Service;

public interface ServiceService
{
    void allowDeployment(Service service);

    void blockDeployment(Service service);

    void redeploy(Service service);

    void updateLabels(Service service);

    void delete(Service service);

    Service getByName(String name);

    List<Service> getByImage(String image);
}