package com.practicum.neuron.service;

import com.practicum.neuron.entity.account.SecurityInfo;
import com.practicum.neuron.entity.account.User;
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
    User getUser(String username);

    /**
     * 获取用户安全绑定信息
     *
     * @param username 用户名
     * @return 用户安全绑定信息 security info
     */
    SecurityInfo getSecurityInfo(String username);


    /**
     * 注册用户
     *
     * @param user      用户数据
     * @param securityInfo 用户安全绑定信息
     * @throws UserExistException 用户已存在
     */
    void register(User user, SecurityInfo securityInfo)throws UserExistException;

    /**
     * 修改密码
     *
     * @param user 用户数据
     * @throws SamePasswordException 新旧密码一致
     * @throws UserNotExistException 用户不存在
     */
    void changePassword(User user) throws SamePasswordException, UserNotExistException;


    /**
     * 删除用户
     *
     * @param user 用户数据
     * @throws UserNotExistException 用户不存在
     */
    void deleteUser(User user) throws UserNotExistException;
}
