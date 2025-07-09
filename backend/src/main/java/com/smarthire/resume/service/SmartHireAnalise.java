package com.smarthire.resume.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.CandidatoRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRequisitosRepository;
import com.smarthire.resume.exception.FlaskConnectionException;
import com.smarthirepro.core.service.impl.AnaliseTemplate;
import com.smarthirepro.domain.model.Candidato;

@Service
public class SmartHireAnalise extends AnaliseTemplate<Vaga> {

    @Autowired
    private VagaRequisitosRepository vagaRequisitosRepository;
    @Autowired
    private CandidatoRepositoryJpa candidatoRepository;
    @Autowired
    private PromptService promptService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<String> definirCriterios(UUID id) {
        VagaRequisitosModel vagaRequisitos = vagaRequisitosRepository.findByCargo_Id(id);
        if (vagaRequisitos == null) {
            throw new IllegalArgumentException("Vaga não encontrada ou sem requisitos definidos.");
        }

        List<String> criterios = new ArrayList<>();

        criterios.add("Habilidades: " + vagaRequisitos.getHabilidades());
        criterios.add("Experiência: " + vagaRequisitos.getExperiencia());
        criterios.add("Formação Acadêmica: " + vagaRequisitos.getFormacaoAcademica());
        criterios.add("Idiomas: " + vagaRequisitos.getIdiomas());

        criterios.add("Peso Habilidades: " + vagaRequisitos.getPesoHabilidades());
        criterios.add("Peso Experiência: " + vagaRequisitos.getPesoExperiencia());
        criterios.add("Peso Formação Acadêmica: " + vagaRequisitos.getPesoFormacaoAcademica());
        criterios.add("Peso Idiomas: " + vagaRequisitos.getPesoIdiomas());

        return criterios;
    }

    @Override
    public String executarAnalise(UUID id, List<String> criterios) {
        String flaskUrl = "http://localhost:5000/compare_resumes";

        List<Candidato> candidatos = candidatoRepository.findByCargo_Id(id);

        List<String> analiseRequest = promptService.generateGeminiPromptsToComparation(candidatos, criterios);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<String>> requestEntity = new HttpEntity<>(analiseRequest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl,
                    requestEntity, String.class);

            return response.getBody();
        } catch (RestClientException ex) {
            throw new FlaskConnectionException("Erro ao se conectar com o serviço externo");
        } catch (Exception ex) {
            throw new FlaskConnectionException("Erro inesperado ao buscar pontuação dos candidatos");
        }
    }

    @Override
    public String criarRelatorio(String resultado) {
        return resultado;
    }
}