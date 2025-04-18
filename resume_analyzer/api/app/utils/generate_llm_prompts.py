def generate_gemini_prompt(text):
    prompt = f"""
    Com base no texto a seguir, extraia a experiência profissional do candidato, seguindo o seguinte formato:
    - Cargo: [cargo]
    - Tempo de experiência: [tempo de experiência]
    texto: {text}
    """
    return prompt.strip()