package org.example.springsession.login.reader;

public class AuthenticationTokenReaderException extends RuntimeException {
    public AuthenticationTokenReaderException() {
        super();
    }

    public AuthenticationTokenReaderException(String message) {
        super(message);
    }

    public AuthenticationTokenReaderException(Throwable t) {
        super(t);
    }

    public AuthenticationTokenReaderException(String s, Throwable t) {
        super(s, t);
    }
}
