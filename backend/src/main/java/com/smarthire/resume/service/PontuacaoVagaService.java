package com.smarthire.resume.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthire.resume.domain.DTO.CandidateScoreDTO;
import com.smarthire.resume.exception.FlaskConnectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PontuacaoVagaService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<CandidateScoreDTO> obterPontuacoesDeCandidatos(UUID vagaId) {
        String flaskUrlComparacao = "http://localhost:5000/compare_resumes?vaga_id=" + vagaId.toString();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(flaskUrlComparacao, String.class);

            Map<String, List<CandidateScoreDTO>> responseMap = objectMapper.readValue(
                    response.getBody(), new TypeReference<>() {}
            );

            List<CandidateScoreDTO> candidatos = responseMap.get("candidates");
            if (candidatos == null) {
                throw new FlaskConnectionException("Resposta do serviço externo não contém dados de candidatos");
            }
            return candidatos;

        } catch (RestClientException ex) {
            throw new FlaskConnectionException("Erro ao se conectar com o serviço externo");
        } catch (IOException ex) {
            throw new FlaskConnectionException("Erro ao processar JSON de resposta");
        } catch (Exception ex) {
            throw new FlaskConnectionException("Erro inesperado ao buscar pontuação dos candidatos");
        }
    }
}
