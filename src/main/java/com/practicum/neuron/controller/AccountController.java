package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.ResponseBody;
import com.practicum.neuron.entity.SecurityInfoDto;
import com.practicum.neuron.entity.Status;
import com.practicum.neuron.entity.UserDto;
import com.practicum.neuron.exception.UserExistException;
import com.practicum.neuron.service.AccountService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 账户系统控制器
 */
@Slf4j
@RestController
public class AccountController {
    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    AccountService accountService;

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

    @PostMapping("${api.account.register}")
    public org.springframework.http.ResponseEntity<ResponseBody> register(HttpServletRequest request) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();
        String email = jsonNode.get("email").asText();
        String role = jsonNode.get("role").asText();
        // 对密码进行加密
        String encodePassword = passwordEncoder.encode(password);
        UserDto user = new UserDto(username, encodePassword, role);
        SecurityInfoDto info = SecurityInfoDto.builder()
                .email(email)
                .build();
        try {
            accountService.register(user, info);
            return new org.springframework.http.ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS),
                    HttpStatus.OK
            );
        }
        catch (UserExistException e) {
            return new org.springframework.http.ResponseEntity<>(
                    new ResponseBody(Status.REGISTER_USER_EXIST),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return new org.springframework.http.ResponseEntity<>(
                    new ResponseBody(Status.REGISTER_UNKNOWN_ERROR),
                    HttpStatus.OK
            );
        }

    }

    @GetMapping("/api/hello")
    public ResponseEntity<ResponseBody> hello() {
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS),
                HttpStatus.OK
        );
    }

    @GetMapping("/api/admin/")
    public ResponseEntity<ResponseBody> admin() {
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, "请求的的资源"),
                HttpStatus.OK
        );
    }

    @GetMapping("/api/user/")
    public ResponseEntity<ResponseBody> user() {
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, "请求的的资源"),
                HttpStatus.OK
        );
    }
}
