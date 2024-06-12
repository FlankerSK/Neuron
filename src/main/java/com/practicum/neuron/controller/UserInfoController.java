package com.practicum.neuron.controller;

import com.practicum.neuron.entity.account.UserInfo;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.service.UserInfoService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/account")
public class UserInfoController {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserInfoService userInfoService;

    @GetMapping
    public ResponseBody getUserInfo(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        UserInfo userInfo = userInfoService.getUserInfo(username);
        return new ResponseBody(Status.SUCCESS, userInfo);
    }

    @PutMapping
    public ResponseBody setUserInfo(@RequestBody UserInfo userInfo) {
        userInfoService.setUserInfo(userInfo);
        return new ResponseBody(Status.SUCCESS);
    }
}
