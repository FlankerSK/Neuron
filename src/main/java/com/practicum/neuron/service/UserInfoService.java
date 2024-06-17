package com.practicum.neuron.service;

import com.practicum.neuron.entity.account.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserInfo getUserInfo(String username);


    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     */
    void setUserInfo(UserInfo userInfo);
}
