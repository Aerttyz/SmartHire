package com.smarthire.resume.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.smarthire.resume.domain.model.UserDetailsImpls;

public class AuthUtils {
    
        public static UUID getEmpresaId() {
            Authentication authentication = 
            SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpls userDetails = 
                (UserDetailsImpls) authentication.getPrincipal();
            UUID empresaId = userDetails.getId();
            if (empresaId == null) {
                throw new IllegalStateException("Empresa ID não encontrado no contexto de autenticação.");
            }
            return empresaId;
        }
}
