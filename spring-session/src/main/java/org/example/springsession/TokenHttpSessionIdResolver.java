package org.example.springsession;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TokenHttpSessionIdResolver {

    private static final Logger logger = LoggerFactory.getLogger(TokenHttpSessionIdResolver.class);
    private static final String[] ZERO_TOKENS = new String[0];
    private static final String TOKEN_NAME = "dt_token";

    private final boolean useBase64Encoding;
    private final String jvmRoute;

    public TokenHttpSessionIdResolver() {
        this(true, null);
    }

    public TokenHttpSessionIdResolver(String jvmRoute) {
        this(true, jvmRoute);
    }

    public TokenHttpSessionIdResolver(boolean useBase64Encoding, String jvmRoute) {
        this.useBase64Encoding = useBase64Encoding;
        this.jvmRoute = jvmRoute;
    }

    public String resolveSessionId(HttpServletRequest request) {
        Set<String> ret = new HashSet<>();
        sessionIdsFromUrl(request, ret);
        sessionIdsFromHeader(request, ret);
        sessionIdsFromCookie(request, ret);
        return ret.toArray(ZERO_TOKENS)[0];
    }

    @Deprecated
    private void sessionIdsFromUrl(HttpServletRequest request, Set<String> sessionIds) {
        String[] tokens = request.getParameterValues(TOKEN_NAME);
        Arrays.stream(tokens)
                .filter(this::isValid)
                .map(this::useBase64)
                .forEach(t -> {
                    if (sessionIds.isEmpty()) {
                        logger.debug("Token from url is deprecated: '" + request.getRequestURI() + "'.");
                    }
                    sessionIds.add(t);
                });
        checkMostOne(sessionIds);
    }

    private void sessionIdsFromHeader(HttpServletRequest request, Set<String> sessionIds) {
        Enumeration<String> tokens = request.getHeaders(TOKEN_NAME);
        if (tokens != null) {
            while (tokens.hasMoreElements()) {
                String token = tokens.nextElement();
                if (isValid(token)) {
                    sessionIds.add(useBase64(token));
                }
            }
        }
        checkMostOne(sessionIds);
    }

    private void sessionIdsFromCookie(HttpServletRequest request, Set<String> sessionIds) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_NAME.equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    if (isValid(sessionId)) {
                        sessionId = useBase64(sessionId);
                        if (this.jvmRoute != null && sessionId.endsWith(this.jvmRoute)) {
                            sessionId = sessionId.substring(0, sessionId.length() - this.jvmRoute.length());
                        }
                        sessionIds.add(sessionId);
                    }
                }
            }
        }
        checkMostOne(sessionIds);
    }

    private boolean isValid(String token) {
        return token != null && !token.isBlank();
    }

    private String useBase64(String s) {
        String ret = s;
        if (useBase64Encoding) {
            String decoded = base64Decode(s);
            if (!decoded.isEmpty()) {
                ret = decoded;
            }
        }
        return ret;
    }

    private String base64Decode(String s) {
        try {
            byte[] decoded = Base64.getDecoder().decode(s);
            return new String(decoded);
        } catch (Exception e) {
            logger.debug("Unable to Base64 decode value.", e);
            return "";
        }
    }

    private void checkMostOne(Set<String> sessionIds) {
        if (sessionIds.size() > 1) {
            throw new IllegalArgumentException("Too many token.");
        }
    }
}
