package com.smarthire.resume.domain.DTO;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagaResumoDto {
    private UUID id;
    private String nome;
}
