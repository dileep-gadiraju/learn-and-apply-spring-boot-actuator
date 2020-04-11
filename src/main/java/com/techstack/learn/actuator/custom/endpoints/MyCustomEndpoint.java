package com.techstack.learn.actuator.custom.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Karthikeyan Nithiyanandam
 */
@Endpoint(id = "mycustom-endpoint", enableByDefault = true)
@Component
public class MyCustomEndpoint {

    @ReadOperation
    public List<String> getNames() {
        return List.of("Karthikeyan", "Pascal", "Thomas");
    }

}
