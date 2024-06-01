package com.practicum.neuron.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtLogoutHandler implements LogoutHandler {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            // 获取 用户名和 JWT 令牌，包括 refreshToken 和 accessToken
            String accessToken = jwtUtil.getToken(request);
            String username = jwtUtil.getUserNameFromToken(accessToken);
            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
            String refreshToken = jsonNode.path("refresh_token").asText();
            // 注销令牌对
            jwtUtil.blockToken(refreshToken, accessToken, username);
            SecurityContextHolder.clearContext();
        }
        catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
