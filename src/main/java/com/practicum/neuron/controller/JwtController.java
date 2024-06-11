package com.practicum.neuron.controller;

import com.practicum.neuron.entity.response.RespondBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Jwt Token 认证控制器
 */
@RestController
@RequestMapping("/api/token")
public class JwtController {
    @Resource
    JwtUtil jwtUtil;

    /**
     * 刷新 accessToken
     *
     * @param request HTTP请求
     * @return 新的 accessToken
     */
    @PostMapping("/access-token")
    public RespondBody refreshAccessToken(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        return new RespondBody(Status.SUCCESS, jwtUtil.createAccessToken(username));
    }

    /**
     * 刷新 refreshToken
     *
     * @param request HTTP请求
     * @return 新的 refreshToken
     */
    @PostMapping("/refresh-token")
    public RespondBody refreshRefreshToken(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        // 注销令牌
        jwtUtil.blockToken(token, token, username);
        return new RespondBody(Status.SUCCESS, jwtUtil.createAccessToken(username));
    }
}
