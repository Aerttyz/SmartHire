package com.smarthire.resume.domain.repository;

import java.util.List;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.model.Vaga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, UUID> {

    List<Vaga> findByNome(String nomeVaga);
    List<Vaga> findByEmpresa(Empresa empresa);
    List<Vaga> findByNomeContainingIgnoreCaseAndEmpresa(String nome, Empresa empresa);
    boolean existsByNome(String nomeVaga);
    Optional<Vaga> findById(UUID id);
    Optional<Vaga> findByIdAndEmpresa(UUID id, Empresa empresa);
}
