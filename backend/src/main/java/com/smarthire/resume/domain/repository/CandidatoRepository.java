package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, UUID> {

    public Optional<Candidato> findByEmail(String email);
}
