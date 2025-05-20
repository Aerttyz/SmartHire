package com.smarthire.resume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Empresa empresa = empresaRepository.findByEmail(email).get();
        return UserDetailsImpls.build(empresa);        
    }
}
