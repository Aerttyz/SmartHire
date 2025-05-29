package com.smarthire.resume.service;

import com.smarthire.resume.domain.model.AvaliacaoLLM;
import com.smarthire.resume.domain.DTO.CurriculoApiDto;
import com.smarthire.resume.domain.DTO.VagaApiDto;
import com.smarthire.resume.domain.model.Curriculo;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.CurriculoRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
// import com.smarthire.resume.exception.ResourceNotFoundException;
// import org.springframework.web.server.ResponseStatusException;
// import org.springframework.http.HttpStatus;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvaliacaoLLMService {

  private final CurriculoRepository curriculoRepository;
  private final VagaRepository vagaRepository;

  private final RestTemplate restTemplate = new RestTemplate();
  private static final Logger logger = LoggerFactory.getLogger(AvaliacaoLLMService.class);


  private final String flaskUrl = "http://localhost:5000/avaliar";

  @Transactional
  public AvaliacaoLLM avaliarCandidatoParaVaga(UUID vagaId, UUID curriculoId) {
    Curriculo curriculo = curriculoRepository.findById(curriculoId)
      .orElseThrow(() -> new RuntimeException("Curriculo nao encontrado com ID: " + curriculoId));

    Vaga vaga = vagaRepository.findById(vagaId)
      .orElseThrow(() -> new RuntimeException("Vaga nao encontrada com ID: " + vagaId));

    CurriculoApiDto curriculoDto = new CurriculoApiDto();
    curriculoDto.setId(curriculo.getId());
    curriculoDto.setExperiencia(curriculo.getExperiencia());
    curriculoDto.setFormacaoAcademica(curriculo.getFormacaoAcademica());
    curriculoDto.setHabilidades(curriculo.getHabilidades());
    curriculoDto.setIdiomas(curriculo.getIdiomas());

    // --- 2. Mapear Entidade Vaga para VagaApiDTO ---
    VagaApiDto vagaDto = new VagaApiDto();
    vagaDto.setId(vaga.getId());
    vagaDto.setNome(vaga.getNome());

    VagaRequisitosModel requisitosEntity = vaga.getRequisitos();
    if (requisitosEntity != null) {
      vagaDto.setRequisitosHabilidades(requisitosEntity.getHabilidades());
      vagaDto.setRequisitosIdiomas(requisitosEntity.getIdiomas());
      vagaDto.setRequisitosFormacaoAcademica(requisitosEntity.getFormacaoAcademica());
      vagaDto.setRequisitosExperiencia(requisitosEntity.getExperiencia());

      vagaDto.setPesoHabilidades(requisitosEntity.getPesoHabilidades());
      vagaDto.setPesoIdiomas(requisitosEntity.getPesoIdiomas());
      vagaDto.setPesoFormacaoAcademica(requisitosEntity.getPesoFormacaoAcademica());
      vagaDto.setPesoExperiencia(requisitosEntity.getPesoExperiencia());
    } else {
      vagaDto.setRequisitosHabilidades("");
      vagaDto.setRequisitosIdiomas("");
      vagaDto.setRequisitosFormacaoAcademica("");
      vagaDto.setRequisitosExperiencia("");
      vagaDto.setPesoHabilidades(0.0);
      vagaDto.setPesoIdiomas(0.0);
      vagaDto.setPesoFormacaoAcademica(0.0);
      vagaDto.setPesoExperiencia(0.0);

      logger.warn("Vaga com ID {} não possui requisitos definidos.", vagaId);

    }

    Map<String, Object> payload = new HashMap<>();
    payload.put("curriculo", curriculoDto);
    payload.put("vaga", vagaDto);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

    ResponseEntity<AvaliacaoLLM> responseEntity;
    try {
      responseEntity = restTemplate.postForEntity(
        flaskUrl, requestEntity, AvaliacaoLLM.class
      );
    } catch (Exception e) {
      logger.error("Erro ao chamar API Python para avaliação: {}", e.getMessage());
      throw new RuntimeException("Erro ao comunicar com o serviço de avaliação de IA.", e);
    }

    return responseEntity.getBody();
  }
}