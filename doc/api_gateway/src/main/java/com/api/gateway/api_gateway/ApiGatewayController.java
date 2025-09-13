package com.api.gateway.api_gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGatewayController {

    @Autowired
    private ApiGatewayRepository agr;
    // private RestTemplate restTemplate;

    @Value("${spring.application.name}")
    private String application;

    @GetMapping("/test")
    public String get() {
        return application;
    }

    @Transactional(readOnly=true)
    @GetMapping
    public List<ApiGatewayEntity> getAll() {
        return agr.findAll();
    }

    @Transactional
    @PostMapping
    public String postMethodName(@RequestBody ApiGatewayEntity entity) {
        agr.save(entity).toString();
        System.out.println("BEFORE EXCEP.: DB SIZE: " + agr.count());
        throw new RuntimeException();
    }
    

    // @GetMapping("/rt/service-a")
    // public String getServiceA() {
    //     return restTemplate.getForObject("http://service-a/", String.class);
    // }
        
    // @GetMapping("/rt/service-b")
    // public String getServiceB() {
    //     return restTemplate.getForObject("http://service-b/", String.class);
    // }

    // @GetMapping("/rt/service-c")
    // public String getServiceC() {
    //     return restTemplate.getForObject("http://service-c/", String.class);
    // }

}
