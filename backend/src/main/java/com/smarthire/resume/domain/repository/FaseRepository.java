package com.smarthire.resume.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smarthire.resume.domain.model.Fase;

@Repository
public interface FaseRepository extends JpaRepository<Fase, UUID> {
    List<Fase> findByVaga_Id(UUID idVaga);
}
