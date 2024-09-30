package org.example.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cookie")
public class CookieController {
    @GetMapping("/add")
    public void add(String domain, String path,
                    String name, String value, Boolean httpOnly, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        if (path != null) {
            cookie.setPath(path);
        }
        if (httpOnly != null) {
            cookie.setHttpOnly(httpOnly);
        }
        response.addCookie(cookie);
    }

    @GetMapping("/remove")
    public void remove(String domain, String path,
                       String name, String value, Boolean httpOnly, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        if (path != null) {
            cookie.setPath(path);
        }
        if (httpOnly != null) {
            cookie.setHttpOnly(httpOnly);
        }
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
