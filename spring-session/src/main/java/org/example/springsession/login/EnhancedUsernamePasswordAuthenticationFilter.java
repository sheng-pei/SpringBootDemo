package org.example.springsession.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsession.login.reader.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.security.auth.Subject;

public class EnhancedUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String UNAUTHENTICATED_USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ATTRIBUTE =
            EnhancedUsernamePasswordAuthenticationFilter.class.getName() + ".request";

    private static final UsernamePasswordAuthenticationToken NULL_TOKEN = new UsernamePasswordAuthenticationToken(null, null) {
        @Override
        public void eraseCredentials() {
        }

        @Override
        public void setDetails(Object details) {
        }

        @Override
        public boolean implies(Subject subject) {
            return false;
        }
    };

    private UnauthenticatedUsernamePasswordAuthenticationTokenReader<?> tokenReader;

    public EnhancedUsernamePasswordAuthenticationFilter() {
        super.setPostOnly(true);
    }

    public EnhancedUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        super.setPostOnly(true);
    }

    public void setTokenReader(UnauthenticatedUsernamePasswordAuthenticationTokenReader<?> tokenReader) {
        this.tokenReader = tokenReader;
    }

//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        return super.requiresAuthentication(request, response);
//    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        if (tokenReader != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) request.getAttribute(
                    UNAUTHENTICATED_USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ATTRIBUTE);
            if (token == null) {
                token = tokenReader.readFrom(request);
                if (token == null) {
                    token = NULL_TOKEN;
                }
                request.setAttribute(UNAUTHENTICATED_USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ATTRIBUTE, token);
            }
            return (String) token.getCredentials();
        } else {
            return super.obtainPassword(request);
        }
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username;
        if (tokenReader != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) request.getAttribute(
                    UNAUTHENTICATED_USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ATTRIBUTE);
            if (token == null) {
                token = tokenReader.readFrom(request);
                if (token == null) {
                    token = NULL_TOKEN;
                }
                request.setAttribute(UNAUTHENTICATED_USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ATTRIBUTE, token);
            }
            username = (String) token.getPrincipal();
        } else {
            username = super.obtainUsername(request);
        }
        request.setAttribute(AjaxAuthenticationFailureHandler.USERNAME_FOR_AJAX_AUTHENTICATION_FAILURE_HANDLER_ATTRIBUTE, username);
        return username;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        if (isAuthenticatedBeforeThisAuthentication(request, response)) {
//            super.getSuccessHandler().onAuthenticationSuccess();
//            return null;
//        }

        try {
            Authentication res = super.attemptAuthentication(request, response);
            request.removeAttribute(AjaxAuthenticationFailureHandler.USERNAME_FOR_AJAX_AUTHENTICATION_FAILURE_HANDLER_ATTRIBUTE);
            return res;
        } catch (ReplayAttackException | InvalidRequestException | LoginStringDecryptedException e) {
            throw new InvalidLoginRequestException("Error when obtaining username and password.", e);
        } finally {
            request.removeAttribute(UNAUTHENTICATED_USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ATTRIBUTE);
        }
    }

//    private boolean isAuthenticatedBeforeThisAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        return super.requiresAuthentication(request, response) && true/*TODO, 已登录校验*/;
//    }

    @Override
    public void setPostOnly(boolean postOnly) {
        if (!postOnly) {
            throw new IllegalArgumentException(
                    "Only HTTP POST requests will be allowed by " +
                            "EnhancedUsernamePasswordAuthenticationFilter.");
        }
    }

}
