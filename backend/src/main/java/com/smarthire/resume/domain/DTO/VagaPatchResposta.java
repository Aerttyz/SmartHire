package com.smarthire.resume.domain.DTO;

import java.util.UUID;

public record VagaPatchResposta(
    
    String nome,
    UUID empresaId,
    boolean isActive,
    String habilidades,
    String idiomas,
    String formacaoAcademica,
    String experiencia,
    Double pesoHabilidades,
    Double pesoIdiomas,
    Double pesoFormacaoAcademica,
    Double pesoExperiencia,
    Double pontuacaoMinima
){}
