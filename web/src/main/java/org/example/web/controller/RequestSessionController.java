package org.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@RequestMapping("/request-session")
public class RequestSessionController {
    public static class SessionObject {
    }

    @Bean
    @SessionScope
    public SessionObject sessionObject() {
        return new SessionObject();
    }

    @Autowired
    private SessionObject sessionObject;

    /**
     * return the same object for a session.
     */
    @GetMapping("/obj")
    public String obj() {
        return sessionObject.toString();
    }

}
