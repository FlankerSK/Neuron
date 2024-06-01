package com.practicum.neuron.config;


import com.practicum.neuron.filter.JwtAuthenticationTokenFilter;
import com.practicum.neuron.filter.JwtLoginFilter;
import com.practicum.neuron.handler.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableGlobalAuthentication
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    // 登陆成功处理器
    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    // 登陆失败处理器
    @Resource
    private LoginFailureHandler loginFailureHandler;

    // 登出处理器
    @Resource
    private JwtLogoutHandler logoutHandler;

    @Resource
    private JwtLogoutSuccessHandler logoutSuccessHandler;

    // 无权访问处理器
    @Resource
    private NoAuthorityHandler noAuthorityHandler;

    // JWT 拦截器
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager);
        jwtLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return http
                //禁用HttpBasic认证
                .httpBasic(AbstractHttpConfigurer::disable)
                //禁用默认登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                //禁用默认登出页面，设置自定义登出处理器
                .logout(
                        logout -> logout
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(logoutSuccessHandler)
                )
                //禁用session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //设置权限
                .authorizeHttpRequests(
                        auth -> auth
                                // /refresh 负责刷新access_token
                                .requestMatchers("/refresh", "/logout").authenticated()
                                // /admin/ 下是管理员用户的资源区域
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                // /user/ 下是普通用户的资源区域
                                .requestMatchers("/user/**").hasRole("USER")
                                // 其他区域无需认证
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                //自定义用户认证失败的处理器
                .exceptionHandling(
                        exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                                .authenticationEntryPoint(authenticationEntryPoint())
                                .accessDeniedHandler(noAuthorityHandler)
                )
                //添加JWT的处理过滤器
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, JwtLoginFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new SecurityAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
