package com.practicum.neuron.filter;

import com.practicum.neuron.handler.BadTokenHandler;
import com.practicum.neuron.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 登录授权过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    // Token 错误处理器
    @Resource
    private BadTokenHandler badTokenHandler;

    // 用户详细信息服务
    @Resource
    private UserDetailsService userDetailsService;

    // JWT 工具类
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 从请求中获取 JWT 令牌，并根据令牌获取用户信息，最后将用户信息封装到 Authentication 中，方便后续校验（只会执行一次）
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet 异常
     * @throws IOException IO 异常
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // 获取 JWT 令牌
        String authToken = jwtUtil.getToken(request);
        if (authToken != null ) {
            try {
                String username = jwtUtil.getUserNameFromToken(authToken);
                // 如果用户名不为空且 SecurityContextHolder 中的 Authentication 为空（表示该用户未登录）
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        // 从数据库中查询用户信息，如果没有对应的记录会抛出异常
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        // 如果 JWT 令牌有效
                        if (jwtUtil.validateToken(authToken, username)) {
                            // 将用户信息封装到 UsernamePasswordAuthenticationToken 对象中
                            // 因为 JWT 令牌中没有密码，所以这里传 null
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                            // 将请求中的详细信息（即：IP、SessionId 等）封装到 UsernamePasswordAuthenticationToken 对象中方便后续校验
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            // 将 UsernamePasswordAuthenticationToken 对象封装到 SecurityContextHolder 中方便后续校验
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                        else {
                            badTokenHandler.handleInvalidTokenException(response);
                            return;
                        }
                    }
                    catch (UsernameNotFoundException e) {
                        log.info(e.getMessage());
                    }
                }
            }
            catch (ExpiredJwtException e) {
                badTokenHandler.handleCredentialExpiredException(response);
                return;
            }
            catch (Exception e) {
                badTokenHandler.handleInvalidTokenException(response);
                return;
            }
        }
        // 放行，执行下一个过滤器
        filterChain.doFilter(request,response);
    }
}

