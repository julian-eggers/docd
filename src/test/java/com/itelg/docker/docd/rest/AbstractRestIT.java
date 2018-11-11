package com.itelg.docker.docd.rest;

import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.test.support.AbstractIT;

public abstract class AbstractRestIT extends AbstractIT
{
    @Autowired
    protected TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    protected int port;

    protected Service redeploy(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/service/" + service + "/redeploy", Service.class);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    protected void redeployWithNotFound(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/service/" + service + "/redeploy", Service.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    protected Service allow(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/service/" + service + "/allow", Service.class);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    protected void allowWithNotFound(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/service/" + service + "/allow", Service.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    protected Service block(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/service/" + service + "/block", Service.class);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    protected void blockWithNotFound(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/service/" + service + "/block", Service.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    protected void delete(String service)
    {
        restTemplate.delete("http://localhost:" + port + "service/" + service);
    }

    protected Service get(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "service/" + service, Service.class);
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    protected void getWithNotFound(String service)
    {
        ResponseEntity<Service> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "service/" + service, Service.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
