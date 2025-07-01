package com.smarthire.resume.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smarthire.resume.domain.model.Vaga;
import com.smarthirepro.domain.model.Empresa;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, UUID> {

    List<Vaga> findByNome(String nomeVaga);

    List<Vaga> findByNomeContainingIgnoreCaseAndEmpresaId(String nome, UUID empresaId);

    boolean existsByNome(String nomeVaga);

    Optional<Vaga> findById(UUID id);

    List<Vaga> findByEmpresaId(UUID empresaId);

//    Optional<Vaga> findByIdAndEmpresa(UUID id, Empresa empresa);

    @Query("SELECT v.pontuacaoMinima FROM Vaga v WHERE v.id = :vagaId")
    Double findPontuacaoMinimaById(@Param("vagaId") UUID vagaId);

    @Query("SELECT v.id FROM Vaga v where v.empresaId = :empresaId")
    List<UUID> findVagaIdsByEmpresaId(@Param("empresaId") UUID empresaId);

    double countByEmpresaId(UUID empresaId);

    void deleteAllByEmpresaId(UUID empresaId);

}
