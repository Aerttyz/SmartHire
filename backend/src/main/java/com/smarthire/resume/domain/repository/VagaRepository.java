package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    Optional<Vaga> findByNome(String nomeVaga);
    boolean existsByNome(String nomeVaga);
    void deleteByNome(String nomeVaga);
}
