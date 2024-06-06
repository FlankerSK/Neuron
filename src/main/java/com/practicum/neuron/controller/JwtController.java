package com.practicum.neuron.controller;

import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Jwt Token 认证控制器
 */
@Controller
public class JwtController {
    @Resource
    JwtUtil jwtUtil;

    /**
     * 刷新 accessToken
     *
     * @param request HTTP请求
     * @return 新的 accessToken
     */
    @PostMapping("${api.refresh.access-token}")
    public ResponseEntity<ResponseBody> refreshAccessToken(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        ResponseBody data = new ResponseBody(Status.SUCCESS, jwtUtil.createAccessToken(username));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * 刷新 refreshToken
     *
     * @param request HTTP请求
     * @return 新的 refreshToken
     */
    @PostMapping("${api.refresh.refresh-token}")
    public ResponseEntity<ResponseBody> refreshRefreshToken(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        ResponseBody data = new ResponseBody(Status.SUCCESS, jwtUtil.createAccessToken(username));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
