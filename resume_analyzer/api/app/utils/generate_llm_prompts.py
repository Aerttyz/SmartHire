from datetime import date

def generate_gemini_prompt(text):
    date_today = date.today().strftime("%d/%m/%Y")
    prompt = f"""
    Data atual{date_today}. Retorne apenas um JSON com os anos totais de experiência do candidato:
    {{"anos_totais": "x anos"}}
    Texto: {text}
    """
    return prompt.strip()