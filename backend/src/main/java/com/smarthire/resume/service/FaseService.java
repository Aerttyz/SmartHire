package com.smarthire.resume.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.model.CandidatoFase;
import com.smarthire.resume.domain.model.Fase;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoFaseRepository;
import com.smarthire.resume.domain.repository.CandidatoRepositoryJpa;
import com.smarthire.resume.domain.repository.EmpresaRepositoryJpa;
import com.smarthire.resume.domain.repository.FaseRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.ItemNotFoundException;
import com.smarthirepro.core.dto.EmailRequest;
import com.smarthirepro.core.exception.BusinessRuleException;
import com.smarthirepro.core.service.IEmailService;
import com.smarthirepro.domain.model.Candidato;
import com.smarthirepro.domain.model.Empresa;

import jakarta.transaction.Transactional;

@Service
public class FaseService {

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private FaseRepository faseRepository;
    @Autowired
    private CandidatoRepositoryJpa candidatoRepository;
    @Autowired
    private CandidatoFaseRepository candidatoFaseRepository;
    @Autowired
    private EmpresaRepositoryJpa empresaRepository;
    @Autowired
    private IEmailService emailService;

    @Transactional
    public void cadastrarFase(UUID id, List<FaseDto> fasesDto) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));

        List<Fase> fases = fasesDto.stream()
                .map(faseDto -> {
                    Fase fase = new Fase();
                    fase.setTitulo(faseDto.titulo());
                    fase.setDescricao(faseDto.descricao());
                    fase.setOrdem(faseDto.ordem());
                    fase.setVaga(vaga);
                    return fase;
                })
                .collect(Collectors.toList());
        faseRepository.saveAll(fases);
    }

    @Transactional
    public void adicionarCandidatoAFase(UUID idFase, List<UUID> idCandidatos) {
        Fase fase = faseRepository.findById(idFase)
                .orElseThrow(() -> new ItemNotFoundException("Fase", idFase));
        Vaga vaga = fase.getVaga();
        List<Candidato> candidatos = candidatoRepository.findAllById(idCandidatos);

        Set<UUID> encontrados = candidatos.stream()
                .map(Candidato::getId)
                .collect(Collectors.toSet());

        List<UUID> naoEncontrados = idCandidatos.stream()
                .filter(id -> !encontrados.contains(id))
                .collect(Collectors.toList());

        if (!naoEncontrados.isEmpty()) {
            throw new BusinessRuleException("Candidatos não encontrados: " + naoEncontrados);
        }

        List<CandidatoFase> candidatosNaFase = candidatoFaseRepository.findByCandidato_IdIn(idCandidatos);

        Map<UUID, CandidatoFase> candidatosNaFaseMap = candidatosNaFase.stream()
                .collect(Collectors.toMap(candidatoFase -> candidatoFase.getCandidato().getId(),
                        candidatoFase -> candidatoFase));

        List<CandidatoFase> atualizarCandidatoNaFase = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            CandidatoFase candidatoFase = candidatosNaFaseMap.get(candidato.getId());

            if (candidatoFase != null && candidatoFase.getFase().getId().equals(idFase)) {
                throw new BusinessRuleException("Candidato já está na fase: " + candidato.getId());
            }
            if (candidatoFase != null) {
                candidatoFase.setFase(fase);
                candidatoFase.setDataInicio(LocalDateTime.now());
                atualizarCandidatoNaFase.add(candidatoFase);
            } else {
                candidatoFase = new CandidatoFase();
                candidatoFase.setCandidato(candidato);
                candidatoFase.setFase(fase);
                candidatoFase.setDataInicio(LocalDateTime.now());
                atualizarCandidatoNaFase.add(candidatoFase);
            }
            enviarEmailNotificacaoDeFase(candidato, fase, vaga);
        }

        candidatoFaseRepository.saveAll(atualizarCandidatoNaFase);
    }

    private void enviarEmailNotificacaoDeFase(Candidato candidato, Fase fase, Vaga vaga) {
        if (candidato.getEmail() != null && !candidato.getEmail().isEmpty()) {
            Empresa empresa = empresaRepository.findById(vaga.getEmpresa().getId())
                    .orElseThrow(() -> new BusinessRuleException(
                            "Não foi possível encontrar os dados da empresa para a vaga."));
            String assunto = "Você avançou para a próxima fase da vaga '" + vaga.getNome() + "'";
            String corpo = String.format(
                    "Olá %s,\n\n" +
                            "Parabéns! Você avançou para a fase '%s' no processo seletivo da vaga '%s' na empresa %s.\n"
                            +
                            "Estamos felizes com seu progresso e em breve você receberá mais informações sobre esta etapa.\n\n"
                            +
                            "Boa sorte!\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe %s",
                    candidato.getNome() != null ? candidato.getNome() : "Candidato",
                    fase.getTitulo(),
                    vaga.getNome(),
                    empresa.getNome(),
                    empresa.getNome());
            EmailRequest request = new EmailRequest(candidato.getEmail(), assunto, corpo);
            emailService.enviarEmail(request);
        }
    }
}
