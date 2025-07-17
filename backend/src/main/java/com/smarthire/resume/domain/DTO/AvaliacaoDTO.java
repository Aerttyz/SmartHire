package com.smarthire.resume.domain.DTO;

import java.util.List;

import com.smarthirepro.core.dto.AvaliacaoDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoDTO extends AvaliacaoDto {
    private String compatibilidade;
    private List<String> pontosFortes;
    private List<String> lacunasIdentificadas;
    private String sugestoesParaEmpresa;
}
