package com.smarthire.resume.domain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.resume.domain.model.CandidatoFase;

import java.util.UUID;
import java.util.List;


@Repository
public interface CandidatoFaseRepository extends JpaRepository<CandidatoFase, UUID> {
    List<CandidatoFase> findByCandidato_IdIn(List<UUID> idCandidatos);

    List<CandidatoFase> findByFase_Id(UUID idFase);
}
