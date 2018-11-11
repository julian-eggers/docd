package com.itelg.docker.docd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.itelg.docker.docd.repository.ServiceRepository;
import com.itelg.docker.docd.repository.impl.DockerClientServiceRepository;

@Profile("!it")
@Configuration
public class DockerClientConfiguration
{
    @Bean
    public DockerClient dockerClient()
    {
        return DockerClientBuilder.getInstance().build();
    }

    @Bean
    public ServiceRepository serviceRepository(DockerClient dockerClient)
    {
        return new DockerClientServiceRepository(dockerClient);
    }
}