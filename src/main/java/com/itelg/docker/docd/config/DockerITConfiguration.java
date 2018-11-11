package com.itelg.docker.docd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.itelg.docker.docd.repository.ServiceRepository;
import com.itelg.docker.docd.repository.impl.DockerITServiceRepository;

@Profile("it")
@Configuration
public class DockerITConfiguration
{
    @Bean
    public ServiceRepository serviceRepository()
    {
        return new DockerITServiceRepository();
    }
}