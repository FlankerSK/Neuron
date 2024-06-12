package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.account.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {
    /**
     * 增加用户信息
     *
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     */
    void addUserInfo(String username, String email, String phone);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserInfo findUserInfoByUsername(@Param("username") String username);

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     */
    void updateUserInfo(UserInfo userInfo);
}
