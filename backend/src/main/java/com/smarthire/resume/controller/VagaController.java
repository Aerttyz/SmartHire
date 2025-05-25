package com.smarthire.resume.controller;

import com.smarthire.resume.domain.DTO.CandidateScoreDTO;
import com.smarthire.resume.domain.DTO.VagaDto;
import com.smarthire.resume.domain.DTO.VagaRespostaDto;
import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.security.jwt.JwtUtils;
import com.smarthire.resume.service.PontuacaoVagaService;
import com.smarthire.resume.service.VagaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/vagas")
public class VagaController {
    @Autowired
    private VagaService vagaService;
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private PontuacaoVagaService pontuacaoVagaService;
  @Autowired
  private JwtUtils jwtUtils;

  // ----------------------- bloco de implementações para o front ---------------------------------------

//  @GetMapping("/me")
//  public ResponseEntity<List<VagaRespostaDto>> listarMinhasVagas(Authentication authentication) {
//    List<VagaRespostaDto> vagas = vagaService.listarTodasAutenticado(authentication);
//    return ResponseEntity.ok(vagas);
//  }
//
//  @GetMapping("/me/{nome}")
//  public ResponseEntity<List<VagaRespostaDto>> buscarMinhasVagasPorNome(@PathVariable String nome, Authentication authentication) {
//    List<VagaRespostaDto> vagas = vagaService.listarPorNomeAutenticado(nome, authentication);
//    return ResponseEntity.ok(vagas);
//  }
//
//  @PostMapping("/me")
//  public ResponseEntity<?> adicionarVaga(Authentication authentication, @Valid @RequestBody VagaDto vaga) {
//    vagaService.salvarAutenticado(vaga, authentication);
//    return ResponseEntity.ok("Vaga cadastrada com sucesso");
//  }
//
//  @PutMapping("/me/{id}")
//  public ResponseEntity<VagaRespostaDto> atualizarVaga(@PathVariable UUID id, @Valid @RequestBody VagaDto data, Authentication authentication) {
//    VagaRespostaDto vagaAtualizada = vagaService.atualizarVagaAutenticado(id, data, authentication);
//    return ResponseEntity.ok(vagaAtualizada);
//  }
//
//  @DeleteMapping("/me/{id}")
//  public ResponseEntity<Void> removerVaga(@PathVariable UUID id, Authentication authentication) {
//    vagaService.excluirAutenticado(id, authentication);
//    return ResponseEntity.noContent().build();
//  }


  //--------------------- fim do bloco ------------------------------

   @GetMapping
    public ResponseEntity<List<VagaRespostaDto>> listarTodas() {
        List<VagaRespostaDto> vagas = vagaService.listarTodas();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/me")
    public ResponseEntity<List<VagaRespostaDto>> listarVagasDaEmpresa(HttpServletRequest request) {
      //extrair o token
      String token = request.getHeader("Authorization");
      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
      }

      UUID empresaId = jwtUtils.getIdFromToken(token);
      List<VagaRespostaDto> vagas = vagaService.listarTodasPorEmpresa(empresaId);
      return ResponseEntity.ok(vagas);
    }

    @GetMapping({"/{nomeVaga}"})
    public ResponseEntity<List<VagaRespostaDto>> buscarVaga(@PathVariable String nomeVaga) {
        List<VagaRespostaDto> vagaRespostaDto = vagaService.listarPorNome(nomeVaga);
        return ResponseEntity.ok(vagaRespostaDto);
    }


    @PostMapping
    public ResponseEntity<?> adicionarvaga(@Valid @RequestBody VagaDto vaga,
                                           HttpServletRequest request) {
     //extrair o token
      String token = request.getHeader("Authorization");
      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
      }

      UUID empresaId = jwtUtils.getIdFromToken(token);
      if (vaga.empresaId() != null) {
        if (!empresaId.equals(vaga.empresaId())) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("ID da empresa informado não corresponde ao ID do token.");
        }

        vagaService.salvar(vaga);
      } else {
        // se nao tiver o id explicito (caso do front) ele cria um novo dto
        // com este id
        VagaDto vagaComId = new VagaDto(
          vaga.nome(),
          empresaId,
          vaga.isActive(),
          vaga.habilidades(),
          vaga.idiomas(),
          vaga.formacaoAcademica(),
          vaga.experiencia(),
          vaga.pesoHabilidades(),
          vaga.pesoIdiomas(),
          vaga.pesoFormacaoAcademica(),
          vaga.pesoExperiencia()
        );
        vagaService.salvar(vagaComId);
      }

      return ResponseEntity.ok("Vaga cadastrada com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<VagaRespostaDto> atualizarVagaPorId(@PathVariable UUID id,
                                                              @Valid @RequestBody VagaDto data) {
        VagaRespostaDto vagaAtualizada = vagaService.atualizarVagaPorId(id, data);
        return ResponseEntity.ok(vagaAtualizada);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerVaga(@PathVariable UUID id) {
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint adicionado nesse controller por haver apenas 1 operação de pontuação de candidatos
    @GetMapping("/{idVaga}/pontuacoes")
    public ResponseEntity<List<CandidateScoreDTO>> obterPontuacoesCandidatos(@PathVariable UUID idVaga) {
        List<CandidateScoreDTO> pontuacoes = pontuacaoVagaService.obterPontuacoesDeCandidatos(idVaga);
        return ResponseEntity.ok(pontuacoes);
    }

}
