package com.smarthire.resume.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.smarthire.resume.domain.model.Vaga;
import com.smarthire.resume.domain.repository.CandidatoRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthirepro.core.service.IAvaliacaoLlm;
import com.smarthirepro.domain.model.Candidato;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AvaliacaoPromptService implements IAvaliacaoLlm {

    private final VagaRepository vagaRepository;
    private final CandidatoRepositoryJpa candidatoRepository;

    @Override
    public String avaliacaoPrompt(UUID vagaId, UUID candidatoId) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada com o ID: " + vagaId));

        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado com o ID: " + candidatoId));

        StringBuilder requisitosVaga = new StringBuilder();

        requisitosVaga.append("Habilidades: ").append(vaga.getRequisitos().getHabilidades()).append("\n")
                .append("Experiência: ").append(vaga.getRequisitos().getExperiencia()).append("\n")
                .append("Formação Acadêmica: ").append(vaga.getRequisitos().getFormacaoAcademica()).append("\n")
                .append("Idiomas: ").append(vaga.getRequisitos().getIdiomas()).append("\n");

        StringBuilder dadosCandidato = new StringBuilder();

        dadosCandidato.append("Experiência: ")
                .append(candidato.getCurriculo() != null && candidato.getCurriculo().getExperiencia() != null
                        ? candidato.getCurriculo().getExperiencia()
                        : "")
                .append("\n")
                .append("Formação Acadêmica: ").append(candidato.getCurriculo() != null
                        && candidato.getCurriculo().getFormacaoAcademica() != null
                                ? String.join(", ", candidato.getCurriculo().getFormacaoAcademica())
                                : "")
                .append("\n")
                .append("Habilidades: ").append(candidato.getCurriculo() != null
                        && candidato.getCurriculo().getHabilidades() != null
                                ? String.join(", ", candidato.getCurriculo().getHabilidades())
                                : "")
                .append("\n")
                .append("Idiomas: ")
                .append(candidato.getCurriculo() != null && candidato.getCurriculo().getIdiomas() != null
                        ? String.join(", ", candidato.getCurriculo().getIdiomas())
                        : "")
                .append("\n");

        String prompt = String.format(
                """
                        Você é um especialista em recrutamento e seleção altamente qualificado. Sua tarefa é analisar o seguinte currículo em relação à vaga descrita e fornecer uma avaliação detalhada, objetiva e estruturada.

                        **Informações da Vaga:**
                        %s

                        **Informações do Currículo:**
                        %s

                        **Instruções de Saída OBRIGATÓRIAS:**
                        Responda EXCLUSIVAMENTE com um objeto JSON válido. O objeto JSON deve conter os seguintes campos-chave:
                        1. "compatibilidade": (String) Uma avaliação concisa do nível de compatibilidade geral do candidato com a vaga. Use termos como "Muito Alta", "Alta", "Média-Alta", "Média", "Média-Baixa", "Baixa" ou "Incompatível".
                        2. "pontosFortes": (Array de Strings) Uma lista detalhando os pontos fortes específicos do candidato que se alinham diretamente com os requisitos e pesos da vaga. Cada item da lista deve ser uma frase completa. Se não houver pontos fortes claros, retorne uma lista vazia [].
                        3. "lacunasIdentificadas": (Array de Strings) Uma lista detalhando as lacunas específicas ou áreas onde o candidato não atende, ou atende apenas parcialmente, aos requisitos da vaga, considerando os pesos. Cada item da lista deve ser uma frase completa. Se não houver lacunas claras, retorne uma lista vazia [].
                        4. "sugestoesParaEmpresa": (String) Uma sugestão construtiva e acionável. Pode ser para o candidato (ex: "Considerar cursos em X para complementar a experiência em Y.") ou para a empresa (ex: "Candidato promissor para desenvolvimento interno na área Z se houver abertura para aprendizado.").

                        **Exemplo de formato JSON esperado:**
                        {
                          "compatibilidade": "Média-Alta",
                          "pontosFortes": [
                            "Demonstra experiência relevante em desenvolvimento backend utilizando Java, o que é um requisito chave.",
                            "Possui conhecimento em metodologias ágeis, alinhado com a cultura da empresa."
                          ],
                          "lacunasIdentificadas": [
                            "A experiência com a tecnologia Spring Boot parece ser limitada, sendo este um requisito importante para a vaga.",
                            "Não foram mencionados conhecimentos em testes automatizados, que seriam um diferencial."
                          ],
                          "sugestoesParaEmpresa": "Para o candidato: Recomenda-se aprofundar os estudos e buscar projetos práticos com Spring Boot. Para a empresa: Avaliar a possibilidade de treinamento em Spring Boot caso os demais aspectos do perfil sejam muito aderentes."
                        }

                        Analise cuidadosamente todas as informações fornecidas, incluindo os pesos dos requisitos da vaga, e gere o objeto JSON conforme especificado. Não inclua nenhum texto, explicação ou formatação adicional antes ou depois do objeto JSON.
                        """,
                requisitosVaga, dadosCandidato);
        return prompt.strip();
    }
}
