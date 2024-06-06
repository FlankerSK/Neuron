package com.practicum.neuron.entity.account;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
public class User implements UserDetails {
    private int id;
    private String username;
    private String password;
    private String role;
    private boolean accountNonLocked;
    private boolean enabled;

    /**
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @return 密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return 权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    /**
     * @return 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * @return 登录凭据是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 是否可用
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
