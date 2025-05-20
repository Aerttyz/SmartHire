package com.smarthire.resume.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements  AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Unauthorized");
            try {
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), body);
            } catch (StreamWriteException | DatabindException e) {
                throw new ServletException("Error writing JSON to response", e);
            }
    }
}
