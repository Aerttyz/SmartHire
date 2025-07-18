package com.smarthire.resume.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.resume.domain.model.CandidatoFase;

public interface CandidatoFaseRepository extends JpaRepository<CandidatoFase, UUID> {
    List<CandidatoFase> findByCandidato_IdIn(List<UUID> idCandidatos);

    List<CandidatoFase> findByFase_Id(UUID idFase);
}
