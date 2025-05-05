package com.smarthire.resume.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smarthire.resume.domain.model.Curriculo;
import java.util.UUID;

@Repository
public interface CurriculoRepository extends JpaRepository<Curriculo, UUID> {

}
