package com.smarthire.resume.domain.repository;

import com.smarthire.resume.domain.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
}
