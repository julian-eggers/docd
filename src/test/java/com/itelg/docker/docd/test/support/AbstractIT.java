package com.itelg.docker.docd.test.support;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.itelg.docker.docd.Application;
import com.itelg.docker.docd.repository.impl.DockerITServiceRepository;

@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT implements DomainTestSupport
{
    @Autowired
    protected DockerITServiceRepository repository;

    @Before
    public void cleanUp()
    {
        repository.deleteAll();
    }
}
