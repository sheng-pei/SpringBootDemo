package org.example.springsession.login.reader.random;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class LTRandomFilter extends OncePerRequestFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/random",
            "GET");
    private static final String DEFAULT_PREFIX = "LT-";

    private AntPathRequestMatcher matcher;
    private RandomManager randomManager;

    public LTRandomFilter() {
        this(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public LTRandomFilter(AntPathRequestMatcher matcher) {
        Objects.requireNonNull(matcher);
        this.matcher = matcher;
        RedisRandomManager randomManager = new RedisRandomManager();
        randomManager.setPrefix(DEFAULT_PREFIX);
        this.randomManager = randomManager;
    }

    public void setRandomUrl(String url) {
        this.matcher = new AntPathRequestMatcher(url, "GET");
    }

    public void setRandomManager(RandomManager randomManager) {
        this.randomManager = randomManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!matcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String r = randomManager.create();
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().println(r);
    }
}
