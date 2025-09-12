package com.service_b.service_b;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@RestController
public class ServiceBController {
    
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String get() {
        return "service B";
    }

    @GetMapping("/service/{serviceId}")
    public String getOtherService(@PathVariable("serviceId") String serviceId) {
        System.out.println(serviceId);
        return restTemplate.getForObject("http://" + serviceId + "/", String.class);
    }

    @GetMapping("/all-services")
    public String getOtherServices() {
        return discoveryClient.getServices().toString();
    }

    @GetMapping("/service-instances/{serviceId}")
    public String getOtherServiceInstances(@PathVariable("serviceId") String serviceId) {
        return discoveryClient.getInstances(serviceId).toString();
    }
    
}
