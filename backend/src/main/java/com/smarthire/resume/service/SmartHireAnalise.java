package com.smarthire.resume.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.model.VagaRequisitosModel;
import com.smarthire.resume.domain.repository.VagaRequisitosRepository;
import com.smarthirepro.core.service.impl.AnaliseTemplate;

@Service
public class SmartHireAnalise extends AnaliseTemplate<Vaga> {

    private static final String API_KEY = "230178321";
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
            + API_KEY;
    @Autowired
    private VagaRequisitosRepository vagaRequisitosRepository;

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

    }

    @Override
    public String criarRelatorio(String resultado) {

    }
}