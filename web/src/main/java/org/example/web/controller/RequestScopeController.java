package org.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/request-scope")
public class RequestScopeController {
    public static class RequestObject {
    }

    @Bean
    @RequestScope
    public RequestObject requestObject() {
        return new RequestObject();
    }

    @Autowired
    private RequestObject requestObject;

    /**
     * return the same object for a request.
     */
    @GetMapping("/obj")
    public String obj() {
        return requestObject.toString();
    }

}
