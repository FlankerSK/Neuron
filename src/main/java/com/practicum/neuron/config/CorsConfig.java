package com.practicum.neuron.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 允许所有路径
                .allowedOriginPatterns("*") // 允许所有来源访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION") // 允许的请求方法
                .allowedHeaders("*") // 允许的头信息
                .allowCredentials(true) // 是否允许携带认证信息，例如cookie或HTTP认证及客户端SSL证明等
                .maxAge(3600); // 预检请求的缓存时间（秒），即在这个时间段内，对于相同的跨域请求不会再发送预检请求
    }
}