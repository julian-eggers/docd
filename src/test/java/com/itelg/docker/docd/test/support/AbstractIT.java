package com.itelg.docker.docd.test.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.itelg.docker.docd.Application;
import com.itelg.docker.docd.repository.impl.DockerITServiceRepository;

@ActiveProfiles("it")
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT implements DomainTestSupport
{
    @Autowired
    protected DockerITServiceRepository repository;

    @BeforeEach
    public void cleanUp()
    {
        repository.deleteAll();
    }
}
