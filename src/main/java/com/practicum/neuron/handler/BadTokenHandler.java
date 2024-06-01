package com.practicum.neuron.handler;

import com.practicum.neuron.entity.ResponseBody;
import com.practicum.neuron.entity.Status;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 错误的 JWT Token 处理器
 */
@Component
public class BadTokenHandler {

    /**
     * 处理登录凭据过期错误
     *
     * @param response HTTP 响应
     * @throws IOException IO错误
     */
    public void handleCredentialExpiredException(HttpServletResponse response)
            throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ResponseBody(Status.ACCESS_CREDENTIAL_EXPIRED).toJson());
        response.getWriter().flush();
    }

    /**
     * 处理无效Token
     *
     * @param response HTTP 响应
     * @throws IOException IO错误
     */
    public void handleInvalidTokenException(HttpServletResponse response)
            throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ResponseBody(Status.ACCESS_INVALID_TOKEN).toJson());
        response.getWriter().flush();
    }
}
