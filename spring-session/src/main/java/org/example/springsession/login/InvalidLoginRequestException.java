package org.example.springsession.login;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginRequestException extends AuthenticationException {
    public InvalidLoginRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
