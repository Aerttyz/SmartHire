package com.smarthire.resume.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smarthirepro.domain.model.Empresa;
import com.smarthirepro.domain.repositories.EmpresaRepository;

@Repository
public interface EmpresaRepositoryJpa extends JpaRepository<Empresa, UUID>, EmpresaRepository {

    Optional<Empresa> findByEmail(String email);

    Optional<Empresa> findByCnpj(String cnpj);

    Optional<Empresa> findById(UUID id);

    boolean existsByCnpj(String cnpjEmpresa);

    boolean existsByNome(String nomeEmpresa);

    List<Empresa> findByNomeIgnoreCase(String nome);

    @Override
    Empresa save(Empresa empresa);

}
