package com.smarthire.resume.service;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.smarthire.resume.domain.repository.CandidatoRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.EmptyPathException;
import com.smarthire.resume.exception.FlaskConnectionException;
import com.smarthire.resume.exception.InvalidPathException;
import com.smarthire.resume.exception.PersistenceException;
import com.smarthire.resume.domain.model.Curriculo;
import com.smarthire.resume.domain.repository.CurriculoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;



@Service
public class CurriculoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://localhost:5000/extract_entities";

    @Autowired
    private CurriculoRepository curriculoRepository;
    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private CandidatoService candidatoService;

    public Map<String, Object> analisarCurriculos(String path) {
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
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new InvalidPathException();
            }
            throw new FlaskConnectionException("Erro inesperado no serviço Flask. Tente novamente mais tarde");
        } catch (ResourceAccessException ex) {
            throw new FlaskConnectionException();
        }
    }

    @Transactional
    public List<Curriculo> salvarCurriculo(String pasta, UUID idVaga) {
        Map<String, Object> resultado = analisarCurriculos(pasta);
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
                String tipo = entidade.get(1).toUpperCase(Locale.ROOT);

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
                
                curriculo.setExperiencia(jsonLimpo);
            } catch (Exception e) {
                throw new PersistenceException("Erro ao salvar currículo");
            }

            curriculoRepository.save(curriculo);
            curriculosSalvos.add(curriculo);
            
            candidatoService.criarComCurriculo(curriculo, idVaga);

        }

        return curriculosSalvos;
    }

    public String pegarCaminhoDoCurriculo(MultipartFile file, UUID vagaId) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new EmptyPathException();
        }

        Path tempDir = Files.createTempDirectory("curriculos_" + vagaId + "_" + UUID.randomUUID())
                .toAbsolutePath();
        
        Path caminho = tempDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(caminho.toFile()))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                Path novoCaminho = tempDir.resolve(zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(novoCaminho);
                }else{
                    Files.createDirectories(novoCaminho.getParent());
                    try (OutputStream os = Files.newOutputStream(novoCaminho)){
                        zis.transferTo(os);
                    }
                }
                zis.closeEntry();
            }
        } 

        String nomeOriginal = file.getOriginalFilename();
        if (nomeOriginal != null && nomeOriginal.toLowerCase().endsWith(".zip")) {
            nomeOriginal = nomeOriginal.replaceFirst("(?i)\\.zip$", "");
        }
        System.out.println("Currículo extraído para: " + tempDir.toString() + "\\" + nomeOriginal);
        return tempDir.toString() + "\\" + nomeOriginal;
    }
}
