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