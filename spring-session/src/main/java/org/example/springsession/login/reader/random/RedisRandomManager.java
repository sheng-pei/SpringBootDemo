package org.example.springsession.login.reader.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import ppl.common.utils.security.SecureRandom;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class RedisRandomManager implements RandomManager {

    private static final Logger log = LoggerFactory.getLogger(RedisRandomManager.class);
    private static final int DEFAULT_RANDOM_LENGTH = 15;
    private static final int DEFAULT_MAX_INACTIVE_INTERVAL = 120;

    private StringRedisTemplate redis;
    private String prefix;
    private int randomLength;
    private int maxInactiveInterval;

    public RedisRandomManager() {
        this.randomLength = DEFAULT_RANDOM_LENGTH;
        this.maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL;
    }

    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }

    public void setRandomLength(int randomLength) {
        if (randomLength > 0) {
            this.randomLength = randomLength;
        } else {
            log.info("Ignore non-positive random length.");
        }
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        if (maxInactiveInterval > 0) {
            this.maxInactiveInterval = maxInactiveInterval;
        } else {
            log.info("Ignore non-positive max inactive interval.");
        }
    }

    @Override
    public String create() {
        String r = Base64.getEncoder().encodeToString(
                SecureRandom.def().nextBytes(randomLength));
        String key = prefix + r;
        try {
            redis.opsForValue()
                    .set(key, "", maxInactiveInterval, TimeUnit.SECONDS);
        } catch (Throwable t) {
            log.info("Couldn't save new random: " + key + ".", t);
        }
        return key;
    }

    @Override
    public boolean check(String random) {
        try {
            return redis.opsForValue().getAndDelete(random) != null;
        } catch (Throwable t) {
            log.info("Redis error during checking random.", t);
            return false;
        }
    }

}
