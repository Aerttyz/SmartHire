package com.smarthire.resume.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.CandidatoDto;
import com.smarthire.resume.domain.DTO.FaseDto;
import com.smarthire.resume.domain.DTO.VagaResumoDto;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.model.CandidatoFase;
import com.smarthire.resume.domain.model.Fase;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoFaseRepository;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.domain.repository.FaseRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.ItemNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class FaseService {

    @Autowired
    private CandidatoFaseRepository candidatoFaseRepository;
    @Autowired
    private FaseRepository faseRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EmailService emailService;


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
                .collect(Collectors.toMap(candidatoFase -> candidatoFase.getCandidato().getId(), candidatoFase -> candidatoFase));

        List<CandidatoFase> atualizarCandidatoNaFase = new ArrayList<>();

        for(Candidato candidato : candidatos) {
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

    public List<FaseDto> listarFases(UUID id) {
        vagaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Vaga", id));

        List<Fase> fases = faseRepository.findByVaga_Id(id);
        if (fases.isEmpty()) {
            throw new BusinessRuleException("Nenhuma fase encontrada.");
        }

        return fases.stream()
                .map(fase -> new FaseDto(
                        fase.getTitulo(),
                        fase.getDescricao(),
                        fase.getOrdem()
                ))
                .collect(Collectors.toList());
    }

    public List<CandidatoDto> listarCandidatosNaFase(UUID idFase) {
        faseRepository.findById(idFase)
                .orElseThrow(() -> new BusinessRuleException("Fase não encontrada."));

        try {
            List<CandidatoFase> candidatosNaFase = candidatoFaseRepository.findByFase_Id(idFase);
            if (candidatosNaFase.isEmpty()) {
                throw new BusinessRuleException("Nenhum candidato encontrado na fase.");
            }


            return candidatosNaFase.stream()
                .map(candidatoFase -> new CandidatoDto(
                        candidatoFase.getCandidato().getId(),
                        candidatoFase.getCandidato().getNome(),
                        candidatoFase.getCandidato().getEmail(),
                        candidatoFase.getCandidato().getTelefone(),
                        candidatoFase.getCandidato().getCurriculo().getHabilidades(),
                        candidatoFase.getCandidato().getCurriculo().getIdiomas(),
                        candidatoFase.getCandidato().getCurriculo().getFormacaoAcademica(),
                        candidatoFase.getCandidato().getCurriculo().getExperiencia(),
                        new VagaResumoDto(
                                candidatoFase.getCandidato().getVaga().getId(),
                                candidatoFase.getCandidato().getVaga().getNome()
                        )
                ))
                .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Nenhum candidato encontrado na fase.");
        }
    }
    private void enviarEmailNotificacaoDeFase(Candidato candidato, Fase fase, Vaga vaga) {
        if (candidato.getEmail() != null && !candidato.getEmail().isEmpty()) {
            String assunto = "Você avançou para a próxima fase da vaga '" + vaga.getNome() + "'";
            String corpo = String.format(
                    "Olá %s,\n\n" +
                            "Parabéns! Você avançou para a fase '%s' no processo seletivo da vaga '%s' na empresa %s.\n" +
                            "Estamos felizes com seu progresso e em breve você receberá mais informações sobre esta etapa.\n\n" +
                            "Boa sorte!\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe %s",
                    candidato.getNome() != null ? candidato.getNome() : "Candidato",
                    fase.getTitulo(),
                    vaga.getNome(),
                    vaga.getEmpresa().getNome(),
                    vaga.getEmpresa().getNome()
            );
            emailService.enviarEmailTexto(candidato.getEmail(), assunto, corpo);
        }
    }
}
