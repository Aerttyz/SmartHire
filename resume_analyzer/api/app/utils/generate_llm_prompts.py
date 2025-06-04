from datetime import date
from app.services.gemini_api_service import gemini_response

def generate_gemini_prompt(text):
    date_today = date.today().strftime("%d/%m/%Y")
    prompt = f"""
    Data atual: {date_today}. Retorne apenas um JSON com os anos totais de experiência para cada função exercida pelo candidato, com a respectiva descrição da função e o número de anos em que o candidato exerceu cada função:
    Exemplo de saída:
    {{
        "python": "x anos",
        "gestor empresarial": "y anos",
        "analista de sistemas": "z anos"
    }}
    Texto: {text}
    """
    return prompt.strip()

def generate_gemini_prompt_to_comparation(candidates):

    resultados = []

    for candidato in candidates:
        nome = candidato['nome']
        email = candidato['email']
        comparacao = candidato['comparacao']

        prompt = "Você é um avaliador de compatibilidade entre currículos e vagas de emprego.\n"
        prompt += "Com base nas informações fornecidas, atribua uma pontuação de compatibilidade total entre 0 e 1, considerando os seguintes critérios:\n\n"

        for criterio, info in comparacao.items():
            valor_candidato = info['candidato']
            valor_vaga = info['vaga']
            peso = info['peso']

            prompt += f"- {criterio.capitalize()} (peso: {peso}):\n"
            prompt += f"  - Candidato: {valor_candidato}\n"
            prompt += f"  - Vaga: {valor_vaga}\n\n"

        prompt += (
            "Para cada critério, avalie a compatibilidade entre candidato e vaga de 0 a 1, "
            "multiplique pelo peso, e ao final, retorne apenas a soma total como um número entre 0 e 1 "
            "(ex: 0.75). Responda **apenas** com o número final."
        )

        resposta = gemini_response(prompt)

        try:
            score_text = resposta.text.strip()
            score_total = float(score_text)
        except Exception as e:
            print(f"Erro ao processar resposta para {nome}: {e}")
            score_total = 0.0

        resultados.append({
            "nome": nome,
            "email": email,
            "score_total": round(score_total, 3) 
        })
        print (resposta)
    return resultados

def criar_prompt_para_avaliacao_fit(curriculo_info, vaga_info):
    """
    Cria um prompt para o LLM avaliar a compatibilidade de um currículo com uma vaga
    e retornar um JSON estruturado com compatibilidade, pontos fortes, lacunas identificadas e sugestões para empresa.
    'curriculo_info' e 'vaga_info' são dicionários representando os dados
    recebidos dos DTOs da API Java.
    """

    exp_curr = curriculo_info.get('experiencia', 'Não informado')
    form_curr_list = curriculo_info.get('formacaoAcademica', [])
    hab_curr_list = curriculo_info.get('habilidades', [])
    idi_curr_list = curriculo_info.get('idiomas', [])

    curriculo_str = f"""
    - Experiência Principal: {exp_curr}
    - Formação Acadêmica: {', '.join(form_curr_list) if form_curr_list else 'Não informado'}
    - Habilidades Declaradas: {', '.join(hab_curr_list) if hab_curr_list else 'Não informado'}
    - Idiomas: {', '.join(idi_curr_list) if idi_curr_list else 'Não informado'}
    """

    nome_vaga = vaga_info.get('nome', 'Não informado') 
    req_hab_vaga = vaga_info.get('habilidades', 'Não informado')
    req_idi_vaga = vaga_info.get('idiomas', 'Não informado') 
    req_form_vaga = vaga_info.get('formacaoAcademica', 'Não informado')
    req_exp_vaga = vaga_info.get('experiencia', 'Não informado')

    peso_hab = vaga_info.get('pesoHabilidades', 0.0)
    peso_idi = vaga_info.get('pesoIdiomas', 0.0)
    peso_form = vaga_info.get('pesoFormacaoAcademica', 0.0)
    peso_exp = vaga_info.get('pesoExperiencia', 0.0)

    vaga_str = f"""
    - Título da Vaga: {nome_vaga}
    - Requisitos de Habilidades (Peso: {peso_hab}): {req_hab_vaga}
    - Requisitos de Idiomas (Peso: {peso_idi}): {req_idi_vaga}
    - Requisitos de Formação Acadêmica (Peso: {peso_form}): {req_form_vaga}
    - Requisitos de Experiência (Peso: {peso_exp}): {req_exp_vaga}
    """


    prompt = f"""
    Você é um especialista em recrutamento e seleção altamente qualificado. Sua tarefa é analisar o seguinte currículo em relação à vaga descrita e fornecer uma avaliação detalhada, objetiva e estruturada.

    **Informações da Vaga:**
    {vaga_str}

    **Informações do Currículo:**
    {curriculo_str}

    **Instruções de Saída OBRIGATÓRIAS:**
    Responda EXCLUSIVAMENTE com um objeto JSON válido. O objeto JSON deve conter os seguintes campos-chave:
    1. "compatibilidade": (String) Uma avaliação concisa do nível de compatibilidade geral do candidato com a vaga. Use termos como "Muito Alta", "Alta", "Média-Alta", "Média", "Média-Baixa", "Baixa" ou "Incompatível".
    2. "pontosFortes": (Array de Strings) Uma lista detalhando os pontos fortes específicos do candidato que se alinham diretamente com os requisitos e pesos da vaga. Cada item da lista deve ser uma frase completa. Se não houver pontos fortes claros, retorne uma lista vazia [].
    3. "lacunasIdentificadas": (Array de Strings) Uma lista detalhando as lacunas específicas ou áreas onde o candidato não atende, ou atende apenas parcialmente, aos requisitos da vaga, considerando os pesos. Cada item da lista deve ser uma frase completa. Se não houver lacunas claras, retorne uma lista vazia [].
    4. "sugestoesParaEmpresa": (String) Uma sugestão construtiva e acionável. Pode ser para o candidato (ex: "Considerar cursos em X para complementar a experiência em Y.") ou para a empresa (ex: "Candidato promissor para desenvolvimento interno na área Z se houver abertura para aprendizado.").

    **Exemplo de formato JSON esperado:**
    {{
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
    }}

    Analise cuidadosamente todas as informações fornecidas, incluindo os pesos dos requisitos da vaga, e gere o objeto JSON conforme especificado. Não inclua nenhum texto, explicação ou formatação adicional antes ou depois do objeto JSON.
    """
    return prompt.strip()