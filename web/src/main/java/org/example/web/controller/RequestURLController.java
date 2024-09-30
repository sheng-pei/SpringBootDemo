package org.example.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/url")
public class RequestURLController {
    @GetMapping("/get")
    public Map<String, String> get(HttpServletRequest request) {
        Map<String, String> ret = new HashMap<>();
        ret.put("url", request.getRequestURL().toString());
        ret.put("uri", request.getRequestURI());
        ret.put("query", request.getQueryString());
        ret.put("servlet", request.getServletPath());
        ret.put("path", request.getPathInfo());
        return ret;
    }
}
