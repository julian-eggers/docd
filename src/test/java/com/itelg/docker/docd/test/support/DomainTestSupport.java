package com.itelg.docker.docd.test.support;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.domain.WebHookEvent;

import lombok.SneakyThrows;

public interface DomainTestSupport
{
    default Service getBaseService()
    {
        return getBaseService("cawandu");
    }

    default Service getBaseService(String name)
    {
        Service service = new Service();
        service.setId("123456");
        service.setName(name);
        service.setImage("jeggers/cawandu:latest");

        return service;
    }

    default Service getServiceWithAlreadyDeployed()
    {
        Service service = getBaseService();
        service.addLabel(Service.DEPLOYMENT_COUNT_LABEL, "1");
        return service;
    }

    default WebHookEvent getBaseWebHookEvent()
    {
        WebHookEvent webHookEvent = new WebHookEvent();
        webHookEvent.setNamespace("jeggers");
        webHookEvent.setRepositoryName("cawandu");
        webHookEvent.setImage("jeggers/cawandu:latest");
        webHookEvent.setTag("latest");
        return webHookEvent;
    }

    default WebHookEvent getGithubWebHookEvent()
    {
        var webHookEvent = getBaseWebHookEvent();
        webHookEvent.setNamespace("docker.pkg.github.com/jeggers");
        webHookEvent.setImage("docker.pkg.github.com/jeggers/cawandu:latest");
        return webHookEvent;
    }

    @SneakyThrows
    default String getDockerHubWebHookEventAsJson()
    {
        try (InputStream inputStream = new ClassPathResource("webhookevent_dockerhub.json").getInputStream())
        {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    @SneakyThrows
    default String getGithubWebHookEventAsJson()
    {
        try (InputStream inputStream = new ClassPathResource("webhookevent_github.json").getInputStream())
        {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
