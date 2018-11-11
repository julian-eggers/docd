package com.itelg.docker.docd.test.support;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.itelg.docker.docd.Application;
import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.domain.WebHookEvent;
import com.itelg.docker.docd.repository.impl.DockerITServiceRepository;

import lombok.SneakyThrows;

@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT
{
    @Autowired
    protected DockerITServiceRepository repository;

    @Before
    public void cleanUp()
    {
        repository.deleteAll();
    }

    protected Service getBaseService()
    {
        return getBaseService("cawandu");
    }

    protected Service getBaseService(String name)
    {
        Service service = new Service();
        service.setId("123456");
        service.setName(name);
        service.setImage("jeggers/cawandu:latest");

        return service;
    }

    protected Service getServiceWithAlreadyDeployed()
    {
        Service service = getBaseService();
        service.addLabel(Service.DEPLOYMENT_COUNT_LABEL, "1");
        return service;
    }

    protected WebHookEvent getBaseWebHookEvent()
    {
        WebHookEvent webHookEvent = new WebHookEvent();
        webHookEvent.setNamespace("jeggers");
        webHookEvent.setRepositoryName("cawandu");
        webHookEvent.setImage("jeggers/cawandu:latest");
        webHookEvent.setTag("latest");
        return webHookEvent;
    }

    @SneakyThrows
    protected String getWebHookEventAsJson()
    {
        try (InputStream inputStream = new ClassPathResource("webhookevent.json").getInputStream())
        {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
