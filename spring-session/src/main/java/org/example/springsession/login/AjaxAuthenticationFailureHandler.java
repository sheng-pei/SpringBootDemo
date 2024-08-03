package org.example.springsession.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsession.common.wrapper.R;
import org.example.springsession.common.wrapper.ResponseCode;
import org.example.springsession.login.lock.LoginLockChecker;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import ppl.common.utils.json.JsonUtils;

import java.io.IOException;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public static final String USERNAME_FOR_AJAX_AUTHENTICATION_FAILURE_HANDLER_ATTRIBUTE =
            AjaxAuthenticationFailureHandler.class.getName() + ".request";

    private LoginLockChecker lockChecker;

    public void setLockChecker(LoginLockChecker lockChecker) {
        this.lockChecker = lockChecker;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = (String) request.getAttribute(USERNAME_FOR_AJAX_AUTHENTICATION_FAILURE_HANDLER_ATTRIBUTE);
        request.removeAttribute(USERNAME_FOR_AJAX_AUTHENTICATION_FAILURE_HANDLER_ATTRIBUTE);

        R<Void> err = R.fromException(exception);
        if (err.getCode() == ResponseCode.LOGIN_ERROR && username != null) {
            if (!lockChecker.incr(username)) {
                err = ResponseCode.UNKNOWN.res();
            }
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter()
                .write(JsonUtils.write(err));
    }
}
