package org.example.springsession.login.reader;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;

public interface UnauthenticatedUsernamePasswordAuthenticationTokenReader<H extends HttpSecurityBuilder<H>> {
    void addFilters(H builder);

    UsernamePasswordAuthenticationToken readFrom(HttpServletRequest request);
}
