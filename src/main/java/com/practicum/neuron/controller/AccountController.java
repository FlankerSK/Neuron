package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.account.SecurityInfo;
import com.practicum.neuron.entity.account.User;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账户系统控制器
 */
@Slf4j
@RestController
public class AccountController {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AccountService accountService;

    @GetMapping("/api/token/test")
    public ResponseBody test(HttpServletRequest request) {
        return new ResponseBody(Status.SUCCESS);
    }

    @SneakyThrows
    @PostMapping("/api/account/register")
    public ResponseBody register(HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();
        String email = jsonNode.get("email").asText();
        String role = jsonNode.get("role").asText();
        // 对密码进行加密
        String encodePassword = passwordEncoder.encode(password);
        User user = new User(username, encodePassword, role);
        SecurityInfo info = SecurityInfo.builder()
                .email(email)
                .build();
        accountService.register(user, info);
        return new ResponseBody(Status.SUCCESS);
    }
}
