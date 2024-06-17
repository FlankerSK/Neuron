package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.account.LoginUser;
import com.practicum.neuron.mapper.LoginUserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private LoginUserMapper loginUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser loginUser = loginUserMapper.findUserFromUsername(username);
        if (loginUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return loginUser;
    }
}
