package org.example.springsession.login.reader;

public class CorruptRequestException extends AuthenticationTokenReaderException {
    public CorruptRequestException(Throwable t) {
        super(t);
    }
}
