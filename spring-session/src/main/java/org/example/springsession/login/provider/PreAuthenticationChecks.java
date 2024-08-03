package org.example.springsession.login.provider;

import org.example.springsession.login.lock.IntervalLockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class PreAuthenticationChecks implements UserDetailsChecker {
    private final int interval;

    public PreAuthenticationChecks(int interval) {
        this.interval = interval;
    }

    @Override
    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new IntervalLockedException(interval, "User account is locked.");
        }
    }
}
