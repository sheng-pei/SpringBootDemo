package org.example.springsession.login;

import org.springframework.lang.NonNull;
import org.springframework.session.SessionIdGenerator;
import ppl.common.utils.security.SecureRandom;

import java.util.Base64;

public class TGTSessionIdGenerator implements SessionIdGenerator {
    private static final int DEFAULT_RANDOM_LENGTH = 64;

    private int randomLength;

    public TGTSessionIdGenerator() {
        this.randomLength = DEFAULT_RANDOM_LENGTH;
    }

    public void setRandomLength(int randomLength) {
        if (randomLength > 0) {
            this.randomLength = randomLength;
        }
    }

    @Override
    @NonNull
    public String generate() {
        return "TGC-" + Base64.getEncoder().encodeToString(
                SecureRandom.defStrong().nextBytes(randomLength));
    }
}
