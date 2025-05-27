package com.smarthire.resume.domain.DTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VagaDto (
    @NotBlank(message="O nome da vaga é obrigatório")
    String nome,

    // @NotNull(message="O ID da empresa é obrigatório")
    UUID empresaId,
    // analisar se pode-se colocar um default
    boolean isActive,

    @NotBlank(message= "As habilidades necessárias são obrigatórias")
    String habilidades,

    String idiomas,

    @NotBlank(message = "A formação acadêmica é obrigatória")
    String formacaoAcademica,
    String experiencia,

    @NotNull(message = "O peso das habilidades é obrigatório.")
    @DecimalMin(value = "0.0", message = "O peso das habilidades não pode ser menor que 0.")
    @DecimalMax(value = "1.0", message = "O peso das habilidades não pode ser maior que 1.")
    Double pesoHabilidades,


    @NotNull(message = "O peso dos idiomas é obrigatório.")
    @DecimalMin(value = "0.0", message = "O peso dos idiomas não pode ser menor que 0.")
    @DecimalMax(value = "1.0", message = "O peso dos idiomas não pode ser maior que 1.")
    Double pesoIdiomas,

    @NotNull(message = "O peso da formação acadêmica é obrigatório.")
    @DecimalMin(value = "0.0", message = "O peso da formação acadêmica não pode ser menor que 0.")
    @DecimalMax(value = "1.0", message = "O peso da formação acadêmica não pode ser maior que 1.")
    Double pesoFormacaoAcademica,

    @NotNull(message = "O peso da experiência é obrigatório.")
    @DecimalMin(value = "0.0", message = "O peso da experiência não pode ser menor que 0.")
    @DecimalMax(value = "1.0", message = "O peso da experiência não pode ser maior que 1.")
    Double pesoExperiencia,

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    Double pontuacaoMinima
) {}
