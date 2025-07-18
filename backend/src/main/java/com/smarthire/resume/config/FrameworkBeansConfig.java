package com.smarthire.resume.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smarthire.resume.domain.DTO.AvaliacaoDTO;
import com.smarthirepro.core.service.IAvaliacaoLlm;
import com.smarthirepro.core.service.impl.AvaliacaoLlmImpl;

@Configuration
public class FrameworkBeansConfig {

    @Bean
    public AvaliacaoLlmImpl<AvaliacaoDTO> avaliacaoLlmImpl(IAvaliacaoLlm avaliacaoLlm) {
        return new AvaliacaoLlmImpl<>(avaliacaoLlm, AvaliacaoDTO.class);
    }
}
