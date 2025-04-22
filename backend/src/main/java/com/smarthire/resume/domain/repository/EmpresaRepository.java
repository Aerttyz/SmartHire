package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    public Optional<Empresa> findByCnpj(String cnpj);

    Optional<Empresa> findById(UUID empresaId);

    boolean existsByCnpj(String cnpjEmpresa);

    boolean existsByNome(String nomeEmpresa);

    void deleteByNome(String nomeEmpresa);

    Optional<Empresa> findByNome(String nome);
}
