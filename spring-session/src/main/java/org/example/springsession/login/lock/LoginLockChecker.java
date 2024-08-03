package org.example.springsession.login.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

public class LoginLockChecker {

    private static final Logger log = LoggerFactory.getLogger(LoginLockChecker.class);

    private static final String USER_LOGIN_ERROR_COUNT_PREFIX = "login_error_count:";
    private static final int DEFAULT_MAX_ATTEMPTS = 3;
    private static final int DEFAULT_INTERVAL = 3;

    private static final RedisScript<Void> INCR_SCRIPT = new DefaultRedisScript<>("local k = KEYS[1]\n" +
            "local v = tonumber(ARGV[1])\n" +
            "local ttl = tonumber(ARGV[2])\n" +
            "if v == nil or ttl == nil then\n" +
            "  return { err = 'Not number.' }\n" +
            "end\n" +
            "redis.call('set', k, '0', 'NX', 'EX', ttl * 60)\n" +
            "local reply = redis.call('get', k)\n" +
            "local e = tonumber(reply)\n" +
            "if e == nil then\n" +
            "  return { err = 'Invalid key: ' .. k }\n" +
            "end\n" +
            "if e < v then\n" +
            "  redis.call('incr', k)\n" +
            "end\n" +
            "return { ok = 'OK' }");

    private static final RedisScript<Boolean> CHECK_SCRIPT = new DefaultRedisScript<>("local k = KEYS[1]\n" +
            "local v = tonumber(ARGV[1])\n" +
            "local ttl = tonumber(ARGV[2])\n" +
            "if v == nil or ttl == nil then\n" +
            "  return { err = 'Not number.' }\n" +
            "end\n" +
            "local reply = redis.call('get', k)\n" +
            "local e = tonumber(reply)\n" +
            "if e == nil then\n" +
            "  return true\n" +
            "end\n" +
            "local res = e >= v\n" +
            "if res then\n" +
            "  redis.call('expire', k, ttl * 60)\n" +
            "end\n" +
            "return res");

    private StringRedisTemplate redis;
    private int maxAttempts;
    private int interval;

    public LoginLockChecker() {
        this.maxAttempts = DEFAULT_MAX_ATTEMPTS;
        this.interval = DEFAULT_INTERVAL;
    }

    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void setMaxAttempts(int maxAttempts) {
        if (maxAttempts > 0) {
            this.maxAttempts = maxAttempts;
        }
    }

    public void setInterval(int interval) {
        if (interval > 0) {
            this.interval = interval;
        }
    }

    public boolean check(String username) {
        String key = USER_LOGIN_ERROR_COUNT_PREFIX + username;
        boolean res = true;
        try {
            Boolean b = redis.execute(CHECK_SCRIPT, Collections.singletonList(key), maxAttempts, interval);
            res = b != null && b;
        } catch (Throwable t) {
            log.info("Redis error during check max attempts.", t);
        }
        return res;
    }

    public boolean incr(String username) {
        boolean res = false;
        String key = USER_LOGIN_ERROR_COUNT_PREFIX + username;
        try {
            redis.execute(INCR_SCRIPT, Collections.singletonList(key), maxAttempts, interval);
            res = true;
        } catch (Throwable t) {
            log.info("Redis error during increment attempt count.", t);
        }
        return res;
    }

    public void clear(String username) {
        String key = USER_LOGIN_ERROR_COUNT_PREFIX + username;
        try {
            redis.delete(key);
        } catch (Throwable t) {
            log.info("Redis error during clear login lock status.", t);
        }
    }
}
