package com.smarthire.resume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.AcessDto;
import com.smarthire.resume.domain.DTO.AuthDto;
import com.smarthire.resume.domain.model.UserDetailsImpls;
import com.smarthire.resume.security.jwt.JwtUtils;
import com.smarthire.resume.exception.AuthenticationException;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AcessDto login(AuthDto authDto){
        try {
             UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(authDto.email(),
                    authDto.senha());

            Authentication authentication = authenticationManager.authenticate(userAuth);

            UserDetailsImpls userAuthenticate = (UserDetailsImpls) authentication.getPrincipal();

            String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);

            AcessDto acessDto = new AcessDto(token);
            return acessDto;
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("E-mail ou senha inválidos");
        }
    }
}
