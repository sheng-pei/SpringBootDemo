package org.example.springsession.login;

import org.example.springsession.login.reader.UnauthenticatedUsernamePasswordAuthenticationTokenReader;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class EnhancedLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, EnhancedLoginConfigurer<H>, EnhancedUsernamePasswordAuthenticationFilter> {

    public EnhancedLoginConfigurer() {
        super(new EnhancedUsernamePasswordAuthenticationFilter(), null);
        usernameParameter("username");
        passwordParameter("password");
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(H http) throws Exception {
        getAuthenticationFilter().setTokenReader(http.getSharedObject(
                UnauthenticatedUsernamePasswordAuthenticationTokenReader.class));
        super.configure(http);
    }

    public EnhancedLoginConfigurer<H> usernameParameter(String usernameParameter) {
        getAuthenticationFilter().setUsernameParameter(usernameParameter);
        return this;
    }

    public EnhancedLoginConfigurer<H> passwordParameter(String passwordParameter) {
        getAuthenticationFilter().setPasswordParameter(passwordParameter);
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }
}
