package org.example.springsession.common.wrapper;

import org.example.springsession.login.InvalidLoginRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ppl.common.utils.enumerate.EnumEncoder;
import ppl.common.utils.string.Strings;

import java.util.*;
import java.util.stream.Collectors;

public enum ResponseCode {
    UNKNOWN(0, "未知错误。", Throwable.class),
    OK(1, "成功。"),
    DATA_ERR(2, "数据访问异常。"),

    //user error
    NO_PERMISSION(10000, "无权限。"),
    LOGIN_ERROR(10001, "用户或密码错误。",
            InvalidLoginRequestException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            ProviderNotFoundException.class,
            DisabledException.class,
            AccountExpiredException.class,
            CredentialsExpiredException.class),
    ACCOUNT_LOCKED_ERROR(10002, "账号已被锁定, 请在{}分钟后重试。",
            LockedException.class);

    private static final Logger log = LoggerFactory.getLogger(ResponseCode.class);
    private static final Map<Class<? extends Throwable>, ResponseCode> EXCEPTION_2_RESPONSE_CODE;

    static {
        ResponseCode[] rcs = ResponseCode.values();
        Map<Class<? extends Throwable>, ResponseCode> map = new HashMap<>();
        for (ResponseCode rc : rcs) {
            for (Class<? extends Throwable> clazz : rc.ts) {
                if (map.containsKey(clazz)) {
                    log.warn("Ignore existed exception: '" + clazz.getCanonicalName() + "'.");
                } else {
                    map.put(clazz, rc);
                }
            }
        }
        EXCEPTION_2_RESPONSE_CODE = Collections.unmodifiableMap(map);
    }

    private final int code;
    private final String message;
    private final Set<Class<? extends Throwable>> ts;

    @SafeVarargs
    ResponseCode(int code, String message, Class<? extends Throwable>... ts) {
        this.code = code;
        this.message = message;
        this.ts = Arrays.stream(ts).collect(Collectors.toUnmodifiableSet());
    }

    public <D> R<D> res(Object... info) {
        return new R<>(this, info);
    }

    @EnumEncoder
    public int getCode() {
        return code;
    }

    public String message(Object... info) {
        return Strings.format(message, info);
    }

    public static ResponseCode fromException(Throwable t) {
        Objects.requireNonNull(t);
        Class<?> clazz = t.getClass();
        while (!Object.class.equals(clazz)) {
            ResponseCode rc = EXCEPTION_2_RESPONSE_CODE.get(clazz);
            if (rc != null) {
                return rc;
            }
            clazz = clazz.getSuperclass();
        }
        return ResponseCode.UNKNOWN;
    }
}
