package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.account.SecurityInfo;
import com.practicum.neuron.entity.account.User;
import com.practicum.neuron.entity.account.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * The interface Account mapper.
 */
@Mapper
public interface AccountMapper {

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户对象 user
     */
    User findUserByUsername(@Param("username") String username);

    /**
     * 增加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param role     角色
     */
    void addUser(
            @Param("username") String username,
            @Param("password") String password,
            @Param("role") String role
    );

    /**
     * 增加用户安全绑定信息
     *
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     */
    void addSecurityInfo(
            @Param("username") String username,
            @Param("email") String email,
            @Param("phone") String phone
    );

    /**
     * 查询账户的安全绑定信息
     *
     * @param username 用户名
     * @return 安全绑定信息 DTO
     */
    SecurityInfo findSecurityInfoByUsername(@Param("username") String username);

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param password 密码
     */
    void changePassword(
            @Param("username") String username,
            @Param("password") String password
    );

    /**
     * 删除用户
     *
     * @param username 用户名
     */
    void deleteUserByUsername(@Param("username") String username);

    /**
     * 删除用户安全绑定信息
     *
     * @param username 用户名
     */
    void deleteSecurityInfo(@Param("username") String username);


    /**
     * 根据用户名查询用户信息
     *
     * @param username the username
     * @return the user info
     */
    UserInfo findUserInfoByUsername(@Param("username") String username);
}
