package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.dto.ResponseDto;
import com.practicum.neuron.dto.Status;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Account controller.
 */
@Slf4j
@RestController
public class AccountController {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    JwtUtil jwtUtil;

    /**
     * 刷新 accessToken
     *
     * @param request HTTP请求
     * @return 新的 accessToken
     */
    @PostMapping("/refresh/access-token")
    public ResponseEntity<ResponseDto> refreshAccessToken(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        ResponseDto data = new ResponseDto(Status.SUCCESS, jwtUtil.createAccessToken(username));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Hello response entity.
     *
     * @return the response entity
     */
    @GetMapping("/hello")
    public ResponseEntity<ResponseDto> hello() {
        return new ResponseEntity<>(
                new ResponseDto(Status.SUCCESS),
                HttpStatus.OK
        );

    }

    /**
     * Admin response entity.
     *
     * @return the response entity
     */
    @GetMapping("/admin/")
    public ResponseEntity<ResponseDto> admin() {
        return new ResponseEntity<>(
                new ResponseDto(Status.SUCCESS, "请求的的资源"),
                HttpStatus.OK
        );
    }

    /**
     * User response entity.
     *
     * @return the response entity
     */
    @GetMapping("/user/")
    public ResponseEntity<ResponseDto> user() {
        return new ResponseEntity<>(
                new ResponseDto(Status.SUCCESS, "请求的的资源"),
                HttpStatus.OK
        );
    }
}
