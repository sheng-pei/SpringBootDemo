package org.example.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * When request is forwarded, it is not through filter chain again
 * but keeps parameter and body.
 */
@RestController
@RequestMapping("/f")
public class ForwardController {
    @GetMapping("/b")
    public Integer b(Integer i) {
        return i;
    }

    @GetMapping("/a")
    public void a(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/f/b");
        dispatcher.forward(request, response);
    }

    @GetMapping("/302")
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = new Cookie("TEST", "test");
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:18080/myapp/f/b");
    }
}
