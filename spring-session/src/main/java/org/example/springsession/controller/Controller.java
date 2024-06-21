package org.example.springsession.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class Controller {
    @GetMapping("sessionId")
    public String sessionId(HttpServletRequest request) {
        return request.getSession(true).getId();
    }
}
