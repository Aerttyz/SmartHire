package com.smarthire.resume.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.smarthire.resume.domain.model.Empresa;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserDetailsImpls implements UserDetails{
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String nome;

    private String email;
    
    private String senha;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpls(UUID id,String nome,String email, String senha, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.authorities = authorities;
    }

    public static UserDetailsImpls build(Empresa empresa) {
        return new UserDetailsImpls(
                empresa.getId(),
                empresa.getNome(),
                empresa.getEmail(),
                empresa.getSenha(),
                new ArrayList<>()    
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getId() {
        return id;
    }

}
