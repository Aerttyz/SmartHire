package com.smarthire.resume.domain.DTO;


public record VagaRequisitosDto (
    String habilidades,
    String idiomas,
    String formacaoAcademica,
    String experiencia,

    Double pesoHabilidades,
    Double pesoIdiomas,
    Double pesoFormacaoAcademica,
    Double pesoExperiencia
){}
