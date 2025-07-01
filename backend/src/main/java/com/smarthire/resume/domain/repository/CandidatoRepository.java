package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, UUID> {

    Optional<Candidato> findByEmail(String email);
    List<Candidato> findByNomeContainingIgnoreCase(String nome);
    Optional<Candidato> findById(UUID id);
    boolean existsByNome(String nomeCandidato);
    List<Candidato> findByVaga_Id(UUID vagaId);

    @Query("SELECT c FROM Candidato c WHERE c.vaga.empresaId = :empresaId")
    List<Candidato> findByEmpresaId(@Param("empresaId") UUID empresaId);


    double countByVagaIdIn(List<UUID> vagaIds);
    // vai iterar dentro das vagas da empresa contando (no bd)
    // currículos que estiverem com o id do array de vagas


}
