package com.practicum.neuron.service;

import com.practicum.neuron.entity.SecurityInfoDto;
import com.practicum.neuron.entity.UserDto;
import com.practicum.neuron.exception.SamePasswordException;
import com.practicum.neuron.exception.UserExistException;
import com.practicum.neuron.exception.UserNotExistException;
import org.springframework.stereotype.Service;

/**
 * 账户服务接口
 */
@Service
public interface AccountService {

    /**
     * 获取用户
     *
     * @param username 用户名
     * @return 用户对象 user
     */
    UserDto getUser(String username);

    /**
     * 获取用户安全绑定信息
     *
     * @param username 用户名
     * @return 用户安全绑定信息 security info
     */
    SecurityInfoDto getSecurityInfo(String username);


    /**
     * 注册用户
     *
     * @param userDto      用户数据
     * @param securityInfo 用户安全绑定信息
     * @throws UserExistException 用户已存在
     */
    void register(UserDto userDto, SecurityInfoDto securityInfo)throws UserExistException;

    /**
     * 修改密码
     *
     * @param userDto 用户数据
     * @throws SamePasswordException 新旧密码一致
     * @throws UserNotExistException 用户不存在
     */
    void changePassword(UserDto userDto) throws SamePasswordException, UserNotExistException;


    /**
     * 删除用户
     *
     * @param userDto 用户数据
     * @throws UserNotExistException 用户不存在
     */
    void deleteUser(UserDto userDto) throws UserNotExistException;
}
