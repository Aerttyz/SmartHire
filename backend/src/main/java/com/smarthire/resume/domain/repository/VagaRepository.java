package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, UUID> {

    Optional<Vaga> findByNome(String nomeVaga);
    boolean existsByNome(String nomeVaga);
    Optional<Vaga> findById(UUID id);
}
