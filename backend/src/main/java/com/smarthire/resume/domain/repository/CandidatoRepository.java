package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, UUID> {

    Optional<Candidato> findByEmail(String email);
    Optional<Candidato> findByNome(String nome);
    Optional<Candidato> findById(UUID id);
    boolean existsByNome(String nomeCandidato);
}
