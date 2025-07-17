# app/services/avaliacao_service.py
from flask import jsonify
from app.services.gemini_api_service import gemini_response
from app.logger import logger
import re
import json

from app.exceptions.errors import application_exception 

def gerar_avaliacao_detalhada_llm(prompt):
    """
    Gera uma avaliação detalhada da compatibilidade entre currículo e vaga usando o LLM.
    Espera-se que curriculo_info e vaga_info sejam dicionários
    com os dados relevantes (oriundos dos DTOs da API Java).
    """
    try:
        
        gemini_response_obj = gemini_response(prompt) 
        print (f"Resposta do Gemini: {gemini_response_obj}")
        response_text_raw = gemini_response_obj.text
        json_response = limpar_resposta_gemini(response_text_raw)
        logger.debug(f"Resposta bruta do Gemini: {json_response}")
        return json.loads(json_response.strip())
       

    except application_exception: 
        raise
    except Exception as e:
        logger.exception("Erro inesperado no serviço de avaliação LLM.")
        raise application_exception("Erro interno ao gerar avaliação.", status_code=500)
    


def limpar_resposta_gemini(texto: str) -> str:
    # Remove blocos de código ```json ... ``` ou ``` ... ```
    texto = re.sub(r"^```(?:json)?\s*", "", texto.strip())
    texto = re.sub(r"\s*```$", "", texto)
    return texto