package com.practicum.neuron.handler;

import com.practicum.neuron.entity.account.User;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
        String accessToken = jwtUtil.createAccessToken(user.getUsername());
        // 发送响应
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> data = new HashMap<>();
        data.put("refresh_token", refreshToken);
        data.put("access_token", accessToken);
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        response.getWriter().write(new ResponseBody(Status.SUCCESS, data).toJson());
        response.getWriter().flush();
    }
}
