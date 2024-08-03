package org.example.springsession.login.provider;

import org.example.springsession.login.lock.LoginLockChecker;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

//TODO, 原始密码不超过72字节
public class LockableUserDetailsService implements UserDetailsService {

    private LoginLockChecker lockChecker;

    public void setLockChecker(LoginLockChecker lockChecker) {
        this.lockChecker = lockChecker;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean locked = lockChecker.check(username);
        if (locked) {
            return lockedUser(username);
        }

        User user = mockUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown username.");
        }
        return user;
    }

    private User lockedUser(String username) {
        return new User(username, null,
                true, true, true,
                false, Collections.emptyList());
    }

    private User mockUser(String username) {
        if (username.equals("user@dtstack.com")) {
            return new User(username, "{JhHyFvReGy+Ns5uhnQI6IA9S1sUvSTimbIuG+iVUXt0=}af50153e61bbb69d48593406aed3079c1bffd207f3513fbbf77e2901eb8de5ce", true, true,
                    true, true,
                    Collections.emptyList());
        } else if (username.equals("disabled@dtstack.com")) {
            return new User(username, "", false,
                    true, true, true,
                    Collections.emptyList());
        } else if (username.equals("expired@dtstack.com")) {
            return new User(username, "", true,
                    false, true, true,
                    Collections.emptyList());
        } else if (username.equals("pass_expired@dtstack.com")) {
            return new User(username, "{2XNpPhoXMr8PqOr/3/2LWtiGX0Fx+47W/Q1gRWn8YLk=}9916e7bf3ab6c9d55d915d33fbb7586e8b01ed8221d220f252014c37be11fd67", true,
                    true, false, true,
                    Collections.emptyList());
        }
        return null;
    }
}
