package com.smarthire.resume.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smarthirepro.domain.model.Curriculo;
import com.smarthirepro.domain.repositories.CurriculoRepository;

@Repository
public interface CurriculoRepositoryJpa extends JpaRepository<Curriculo, UUID>, CurriculoRepository {

    @Override
    Curriculo save(Curriculo curriculo);

}