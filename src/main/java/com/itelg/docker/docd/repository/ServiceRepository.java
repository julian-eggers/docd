package com.itelg.docker.docd.repository;

import java.util.List;

import com.itelg.docker.docd.domain.Service;

public interface ServiceRepository
{
    void recreate(Service service);

    void updateLabels(Service service);

    void delete(Service service);

    Service getByName(String name);

    List<Service> getByImage(String image);
}