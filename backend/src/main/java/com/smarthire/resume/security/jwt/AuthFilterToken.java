package com.smarthire.resume.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smarthire.resume.service.UserDetailsServiceImpl;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AuthFilterToken extends  OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      System.out.println("Filtro executado para a URL: " + request.getRequestURI());

      String path = request.getRequestURI();

      if (path.startsWith("/auth") || path.startsWith("/vagas") || path.startsWith("/empresas") || path.startsWith("/candidatos/") || path.startsWith("/curriculos")) {
        filterChain.doFilter(request, response);
        return;
      }

        try {
            String jwt = getToken(request);
            if(jwt != null && jwtUtil.validateToken(jwt)){

                String email = jwtUtil.getEmailToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }catch (Exception e) {
            System.out.println("Erro ao validar token");
        }finally{
            filterChain.doFilter(request, response);
        }
    }


    private String getToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return bearer.replace("Bearer ", "");
        }
        return null;
    }
}
