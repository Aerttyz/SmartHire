package com.smarthire.resume.service;

import java.util.ArrayList;
import java.util.List;

import com.smarthire.resume.domain.enums.Situacao;
import com.smarthire.resume.domain.model.Candidato;
import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.EmptyPathException;
import com.smarthire.resume.exception.FlaskConnectionException;
import com.smarthire.resume.exception.InvalidPathException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthire.resume.domain.model.Curriculo;
import com.smarthire.resume.domain.repository.CurriculoRepository;

@Service
public class CurriculoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://localhost:5000/extract_entities";

    @Autowired
    private CurriculoRepository curriculoRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;

    public Map<String, Object> analyzeFolder(String path) {
        if (path == null || path.isEmpty()) {
            throw new EmptyPathException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = Map.of("path", path);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new InvalidPathException();
        } catch (ResourceAccessException ex) {
            throw new FlaskConnectionException();
        }
    }

    @Transactional
    public List<Curriculo> salvarCurriculo(String pasta) {
        Map<String, Object> resultado = analyzeFolder(pasta);
        Map<String, Object> mapaDeEntidades = (Map<String, Object>) resultado.get("entities");

        List<Curriculo> curriculosSalvos = new ArrayList<>();

        for (Map.Entry<String, Object> entry : mapaDeEntidades.entrySet()) {
            Map<String, Object> curriculoData = (Map<String, Object>) entry.getValue();
            List<List<String>> entidades = (List<List<String>>) curriculoData.get("entities");

            Curriculo curriculo = new Curriculo();

            if (curriculo.getHabilidades() == null)
                curriculo.setHabilidades(new ArrayList<>());
            if (curriculo.getIdiomas() == null)
                curriculo.setIdiomas(new ArrayList<>());

            for (List<String> entidade : entidades) {
                if (entidade.size() < 2)
                    continue;

                String valor = entidade.get(0).trim();
                String tipo = entidade.get(1).toUpperCase();

                switch (tipo) {
                    case "NAME":
                        if (curriculo.getNome() == null)
                            curriculo.setNome(valor);
                        break;
                    case "EMAIL":
                        if (curriculo.getEmail() == null)
                            curriculo.setEmail(valor);
                        break;
                    case "PHONE":
                        if (curriculo.getTelefone() == null)
                            curriculo.setTelefone(valor);
                        break;
                    case "SKILLS":
                        curriculo.getHabilidades().add(valor);
                        break;
                    case "LANGUAGES":
                        curriculo.getIdiomas().add(valor);
                        break;
                    case "EDUCATION":
                        curriculo.getFormacaoAcademica().add(valor);
                        break;
                    default:
                        throw new BusinessRuleException("Tipo inválido: " + tipo);
                }
            }

            String responseStr = (String) curriculoData.get("response");
            try {
                String jsonLimpo = responseStr.replace("```json\n", "").replace("\n```", "");
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> responseMap = mapper.readValue(jsonLimpo, new TypeReference<>() {
                });
                curriculo.setExperiencia(responseMap.get("anos_totais"));
            } catch (Exception e) {
                e.printStackTrace(); 
            }

            curriculoRepository.save(curriculo);
            curriculosSalvos.add(curriculo);
            Candidato candidato = new Candidato();
            candidato.setCurriculo(curriculo);
            candidato.setNome(curriculo.getNome());
            candidato.setEmail(curriculo.getEmail());
            candidato.setTelefone(curriculo.getTelefone());
            candidato.setSituacao(Situacao.TRIAGEM);

            candidatoRepository.save(candidato);

        }

        return curriculosSalvos;
    }

}
