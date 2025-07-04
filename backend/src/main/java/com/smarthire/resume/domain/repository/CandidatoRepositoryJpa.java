package com.smarthire.resume.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.repositories.CandidatoRepository;

@Repository
public interface CandidatoRepositoryJpa extends JpaRepository<Candidato, UUID>, CandidatoRepository {

    @Override
    Candidato save(Candidato candidato);

    @Override
    Optional<Candidato> findByEmail(String email);

    List<Candidato> findByNomeContainingIgnoreCase(String nome);

    Optional<Candidato> findById(UUID id);

    boolean existsByNome(String nomeCandidato);

    List<Candidato> findByCargo_Id(UUID vagaId);

    @Query("SELECT c FROM Candidato c WHERE TYPE(c.cargo) = Vaga AND c.cargo.empresa.id = :empresaId")
    List<Candidato> findByEmpresaId(@Param("empresaId") UUID empresaId);

    double countByCargoIdIn(List<UUID> vagaIds);
    // vai iterar dentro das vagas da empresa contando (no bd)
    // currículos que estiverem com o id do array de vagas

}
