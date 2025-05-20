package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    public Optional<Empresa> findByCnpj(String cnpj);

    Optional<Empresa> findById(UUID id);

    boolean existsByCnpj(String cnpjEmpresa);

    boolean existsByNome(String nomeEmpresa);

    List<Empresa> findByNomeIgnoreCase(String nome);

    Optional<Empresa> findByEmail(String email);
}
