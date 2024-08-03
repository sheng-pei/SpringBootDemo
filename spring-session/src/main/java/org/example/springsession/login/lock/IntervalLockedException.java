package org.example.springsession.login.lock;

import org.example.springsession.common.wrapper.MessageParameter;
import org.springframework.security.authentication.LockedException;

public class IntervalLockedException extends LockedException implements MessageParameter {
    private final int interval;

    public IntervalLockedException(int interval, String msg) {
        super(msg);
        this.interval = interval;
    }

    @Override
    public Object[] params() {
        return new Object[] {interval};
    }
}
