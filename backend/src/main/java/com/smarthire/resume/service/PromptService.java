package com.smarthire.resume.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smarthirepro.domain.model.Candidato;

@Service
public class PromptService {

    public static List<String> generateGeminiPromptsToComparation(List<Candidato> candidatos, List<String> criterios) {
        List<String> prompts = new ArrayList<>();

        String criteriosTexto = String.join("\n", criterios);

        for (Candidato candidato : candidatos) {
            String nome = candidato.getNome();
            String email = candidato.getEmail();

            String experiencia = candidato.getCurriculo() != null && candidato.getCurriculo().getExperiencia() != null
                    ? candidato.getCurriculo().getExperiencia()
                    : "";

            List<String> formacaoAcademica = candidato.getCurriculo() != null
                    && candidato.getCurriculo().getFormacaoAcademica() != null
                            ? candidato.getCurriculo().getFormacaoAcademica()
                            : new ArrayList<>();

            List<String> habilidades = candidato.getCurriculo() != null
                    && candidato.getCurriculo().getHabilidades() != null
                            ? candidato.getCurriculo().getHabilidades()
                            : new ArrayList<>();

            List<String> idiomas = candidato.getCurriculo() != null && candidato.getCurriculo().getIdiomas() != null
                    ? candidato.getCurriculo().getIdiomas()
                    : new ArrayList<>();

            String formacaoTexto = String.join(", ", formacaoAcademica);
            String habilidadesTexto = String.join(", ", habilidades);
            String idiomasTexto = String.join(", ", idiomas);

            String prompt = "Você é um avaliador de compatibilidade entre perfis de candidatos e requisitos de vaga.\n\n"
                    +
                    "Abaixo estão os critérios da vaga:\n" +
                    criteriosTexto +
                    "\n\n" +
                    "E abaixo estão os dados do candidato:\n" +
                    "- Nome: " + nome + "\n" +
                    "- Email: " + email + "\n" +
                    "- Experiência: " + experiencia + "\n" +
                    "- Formação Acadêmica: " + formacaoTexto + "\n" +
                    "- Habilidades: " + habilidadesTexto + "\n" +
                    "- Idiomas: " + idiomasTexto + "\n\n" +
                    "Com base nesses dados, avalie a compatibilidade geral entre o candidato e a vaga, " +
                    "atribuindo uma pontuação entre 0 e 1, considerando os pesos dos critérios quando presentes. " +
                    "Responda apenas com o número e o nome, como por exemplo: josé: 0.83";

            prompts.add(prompt);
        }

        return prompts;
    }
}
