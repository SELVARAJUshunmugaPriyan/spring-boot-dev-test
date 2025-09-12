package com.service_c.service_c;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class ServiceCController {
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String get() {
        return "service C";
    }

    @GetMapping("/service/{serviceId}")
    public String getOtherService(@PathVariable("serviceId") String serviceId) {
        return restTemplate.getForObject("http://" + serviceId + "/", String.class);
    }
    
}
