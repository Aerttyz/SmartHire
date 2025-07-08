package com.smarthire.resume.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smarthire.resume.domain.model.VagaRequisitosModel;

public interface VagaRequisitosRepository extends JpaRepository<VagaRequisitosModel, String> {
    VagaRequisitosModel findByCargo_Id(UUID vagaId);
}
