package com.service_a.service_a;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ServiceAController {
    
    @GetMapping
    public String get() {
        return "Service A";
    }

}
