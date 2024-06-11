package com.practicum.neuron.handler;

import com.practicum.neuron.entity.response.RespondBody;
import com.practicum.neuron.entity.response.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String body;
        if (exception instanceof BadCredentialsException) {
            body = new RespondBody(Status.LOGIN_BAD_CREDENTIAL).toJson();
        }
        else if(exception instanceof AccountExpiredException) {
            body = new RespondBody(Status.LOGIN_ACCOUNT_EXPIRED).toJson();
        }
        else if(exception instanceof DisabledException) {
            body = new RespondBody(Status.LOGIN_ACCOUNT_DISABLED).toJson();
        }
        else if(exception instanceof LockedException) {
            body = new RespondBody(Status.LOGIN_ACCOUNT_LOCKED).toJson();
        }
        else if (exception instanceof CredentialsExpiredException) {
            body = new RespondBody(Status.ACCESS_CREDENTIAL_EXPIRED).toJson();
        }
        else {
            Status status = new Status(Status.LOGIN_UNKNOWN_ERROR);
            status.setMessage(exception.getMessage());
            body = new RespondBody(status).toJson();
        }
        response.getWriter().write(body);
        response.getWriter().flush();
    }
}
