package com.smarthire.resume.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthire.resume.domain.DTO.CandidateScoreDTO;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.FlaskConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EmailService emailService;

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

    public double enviarEmailsParaCandidatosInaptos(UUID vagaId) {
        List<CandidateScoreDTO> candidatos = obterPontuacoesDeCandidatos(vagaId);
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new BusinessRuleException("Vaga não encontrada"));

        Double pontuacaoMinima = vagaRepository.findPontuacaoMinimaById(vagaId);
        String nomeVaga = vaga.getNome();
        String nomeEmpresa = vaga.getEmpresa().getNome();

        candidatos.stream()
                .filter(c -> c.email() != null && !c.email().isEmpty())
                .filter(c -> !pontuacaoMinimaAtingida(c.score_total(), pontuacaoMinima))
                .forEach(c -> {
                    String assunto = "Atualização sobre sua candidatura para '" + nomeVaga + "' - " + nomeEmpresa;

                    String corpo = String.format(
                            "Olá %s,\n\n" +
                                    "Agradecemos seu interesse na vaga '%s' na empresa %s.\n\n" +
                                    "Após análise cuidadosa, informamos que seu perfil não atendeu aos requisitos mínimos para avançar no processo seletivo desta vez.\n\n" +
                                    "Mas não desanime! Continue acompanhando nossas oportunidades e não hesite em se candidatar a outras vagas que melhor se alinhem com suas habilidades.\n\n" +
                                    "Atenciosamente,\n" +
                                    "Equipe %s",
                            c.nome() != null ? c.nome() : "Candidato",
                            nomeVaga,
                            nomeEmpresa,
                            nomeEmpresa
                    );

                    emailService.enviarEmailTexto(c.email(), assunto, corpo);
                });
        return pontuacaoMinima;
    }

    private boolean pontuacaoMinimaAtingida(double scoreCandidato, double pontuacaoMinima) {
        return scoreCandidato >= pontuacaoMinima;
    }
}
