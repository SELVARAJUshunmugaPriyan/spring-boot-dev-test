package com.api.gateway.api_gateway;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;



@RestController
public class ApiGatewayController {

    // @Autowired
    // private RestTemplate restTemplate;

    @GetMapping("/test")
    public String getMethodName() {
        return "Api Gateway";
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
