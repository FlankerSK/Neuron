package com.practicum.neuron.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    static private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        if ("application/json".equals(request.getContentType())) {
            try {
                JsonNode node = objectMapper.readTree(request.getInputStream());
                String username = node.get("username").asText();
                String password = node.get("password").asText();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
                this.setDetails(request, token);
                return this.getAuthenticationManager().authenticate(token);
            }
            catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return super.attemptAuthentication(request, response);
    }
}
