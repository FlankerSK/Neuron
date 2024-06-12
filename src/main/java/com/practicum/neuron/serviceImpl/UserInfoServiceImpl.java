package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.account.UserInfo;
import com.practicum.neuron.mapper.UserInfoMapper;
import com.practicum.neuron.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserInfo(String username) {
        return userInfoMapper.findUserInfoByUsername(username);
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        userInfoMapper.updateUserInfo(userInfo);
    }
}
