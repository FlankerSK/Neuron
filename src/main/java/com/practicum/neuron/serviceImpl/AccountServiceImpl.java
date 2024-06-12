package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.account.SecurityInfo;
import com.practicum.neuron.entity.account.User;
import com.practicum.neuron.exception.SamePasswordException;
import com.practicum.neuron.exception.UserExistException;
import com.practicum.neuron.exception.UserNotExistException;
import com.practicum.neuron.mapper.AccountMapper;
import com.practicum.neuron.mapper.UserInfoMapper;
import com.practicum.neuron.service.AccountService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 账户服务实现
 */
@Slf4j
@Component
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountMapper accountMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public User getUser(String username) {
        return accountMapper.findUserByUsername(username);
    }

    @Override
    public SecurityInfo getSecurityInfo(String username) {
        return accountMapper.findSecurityInfoByUsername(username);
    }

    @Override
    public void register(User user, SecurityInfo securityInfo) throws UserExistException {
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole();
        String email = securityInfo.getEmail();
        String phone = securityInfo.getPhone();
        // 如果用户名已经存在，抛出异常
        if (accountMapper.findUserByUsername(username) != null) {
            throw new UserExistException();
        }
        // 向数据库中添加用户信息与安全绑定信息
        accountMapper.addUser(username, password, role);
        accountMapper.addSecurityInfo(username, email, phone);
        userInfoMapper.addUserInfo(username, email, phone);
    }

    @Override
    public void changePassword(User userDto)
            throws SamePasswordException, UserNotExistException {
        String username = userDto.getUsername();
        User user = accountMapper.findUserByUsername(username);
        // 如果用户不存在，抛出异常
        if (user == null) {
            throw new UserNotExistException();
        }
        String oldPassword = user.getPassword();
        String newPassword = userDto.getPassword();
        // 如果两次密码不一致，抛出异常
        if (oldPassword.equals(newPassword)) {
            throw new SamePasswordException();
        }
        // 修改数据库中的密码
        accountMapper.changePassword(username, newPassword);
    }

    @Override
    public void deleteUser(User userDto) throws UserNotExistException {
        String username = userDto.getUsername();
        User user = accountMapper.findUserByUsername(username);
        // 如果用户不存在，抛出异常
        if (user == null) {
            throw new UserNotExistException();
        }
        // 删除用户信息与安全认证信息
        accountMapper.deleteUserByUsername(username);
        accountMapper.deleteSecurityInfo(username);
    }
}
