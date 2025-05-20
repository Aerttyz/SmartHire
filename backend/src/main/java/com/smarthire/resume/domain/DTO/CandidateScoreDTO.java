package com.smarthire.resume.domain.DTO;

import jakarta.validation.constraints.*;

public record CandidateScoreDTO(
        @Email @NotBlank String email,
        @NotNull String nome,
        @DecimalMin("0.0") @DecimalMax("1.0")
        double score_total
) {
}
