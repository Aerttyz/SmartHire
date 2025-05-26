package com.smarthire.resume.security.jwt;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.smarthire.resume.domain.model.UserDetailsImpls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;


@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${smarthire.app.jwtSecret}")
    private String jwtSecret;

    @Value("${smarthire.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final EmpresaRepository empresaRepository;

    public Empresa extractEmpresaFromToken(String token) {
        String email = extractEmailFromToken(token);
        return empresaRepository.findByEmail(email)
          .orElseThrow(() -> new RuntimeException("Empresa não encontrada por email."));
    }

    public String extractEmailFromToken(String token) {
        Claims claims = Jwts.parser()
          .setSigningKey(jwtSecret.getBytes())
          .parseClaimsJws(token)
          .getBody();

        return claims.getSubject();
    }

    public String generateTokenFromUserDetailsImpl(UserDetailsImpls userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getId());
        claims.put("empresaId", userDetails.getId().toString());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact(); 
        
    }

    public Key getSigningKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return key;
    }

    public String getEmailToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();
        return true;
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build().parseClaimsJws(token)
                    .getBody();
    }

    public UUID getIdFromToken(String token) {
        Claims claims = getClaims(token);
        String idString = claims.get("empresaId", String.class);
        return UUID.fromString(idString);
    }
    
}
