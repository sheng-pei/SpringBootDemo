package org.example.springsession.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsession.common.wrapper.R;
import org.example.springsession.common.wrapper.ResponseCode;
import org.example.springsession.login.lock.LoginLockChecker;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ppl.common.utils.json.JsonUtils;

import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private LoginLockChecker lockChecker;

    public void setLockChecker(LoginLockChecker lockChecker) {
        this.lockChecker = lockChecker;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        lockChecker.clear(authentication.getName());
        R<Void> r = ResponseCode.OK.res();
        r.message("登录成功");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter()
                .write(JsonUtils.write(r));
    }
}
