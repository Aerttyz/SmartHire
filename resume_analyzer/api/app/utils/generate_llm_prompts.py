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

def generate_gemini_prompt_to_comparation(prompts):

    resultados = []

    for prompt in prompts:
        try:
            resposta = gemini_response(prompt)
            score_text = resposta.text.strip()

        except Exception as e:
            print(f"Erro ao processar resposta: {e}")
            score_text = "0.0"

        resultados.append({
            "score_total": score_text
        })
        print(resposta)

    print(f"Resultados: {resultados}")
    return resultados
