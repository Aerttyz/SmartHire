package com.smarthire.resume.domain.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smarthire.resume.domain.model.Fase;
import java.util.UUID;
import java.util.List;

@Repository
public interface FaseRepository extends  JpaRepository<Fase, UUID> {
    List<Fase> findByVaga_Id(UUID idVaga);
}
