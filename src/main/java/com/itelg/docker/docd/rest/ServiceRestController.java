package com.itelg.docker.docd.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itelg.docker.docd.domain.Service;
import com.itelg.docker.docd.service.ServiceService;

@RestController
@RequestMapping("service")
public class ServiceRestController
{
    @Autowired
    private ServiceService serviceService;

    @GetMapping("{name}/allow")
    @PostMapping("{name}/allow")
    public ResponseEntity<Service> allowByName(@PathVariable String name)
    {
        Service service = serviceService.getByName(name);

        if (service == null)
        {
            return ResponseEntity.notFound().build();
        }

        serviceService.allowDeployment(service);
        return ResponseEntity.ok(service);
    }

    @GetMapping("{name}/block")
    @PostMapping("{name}/block")
    public ResponseEntity<Service> blockByName(@PathVariable String name)
    {
        Service service = serviceService.getByName(name);

        if (service == null)
        {
            return ResponseEntity.notFound().build();
        }

        serviceService.blockDeployment(service);
        return ResponseEntity.ok(service);
    }

    @GetMapping("{name}/redeploy")
    @PostMapping("{name}/redeploy")
    public ResponseEntity<Service> redeployByName(@PathVariable String name)
    {
        Service service = serviceService.getByName(name);

        if (service == null)
        {
            return ResponseEntity.notFound().build();
        }

        serviceService.redeploy(service);
        return ResponseEntity.ok(service);
    }

    @DeleteMapping("{name}")
    public ResponseEntity<Service> deleteByName(@PathVariable String name)
    {
        Service service = serviceService.getByName(name);

        if (service == null)
        {
            return ResponseEntity.notFound().build();
        }

        serviceService.delete(service);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{name}")
    public ResponseEntity<Service> getByName(@PathVariable String name)
    {
        Service service = serviceService.getByName(name);

        if (service == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(service);
    }
}
