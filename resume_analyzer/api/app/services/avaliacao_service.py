# app/services/avaliacao_service.py
from app.services import gemini_api_service
from app.utils.generate_llm_prompts import criar_prompt_para_avaliacao_fit # NOVA FUNÇÃO DE PROMPT
from app.logger import logger
import json

from app.exceptions.errors import application_exception 

def gerar_avaliacao_detalhada_llm(curriculo_info, vaga_info):
    """
    Gera uma avaliação detalhada da compatibilidade entre currículo e vaga usando o LLM.
    Espera-se que curriculo_info e vaga_info sejam dicionários
    com os dados relevantes (oriundos dos DTOs da API Java).
    """
    logger.info(f"Iniciando avaliação para currículo ID (do DTO): {curriculo_info.get('id')} e vaga ID (do DTO): {vaga_info.get('id')}")

    try:
        prompt = criar_prompt_para_avaliacao_fit(curriculo_info, vaga_info)

        gemini_response_obj = gemini_api_service.gemini_response(prompt) 

        response_text_raw = gemini_response_obj.text
        logger.debug(f"Resposta bruta do Gemini: {response_text_raw}")

        try:
            cleaned_text = response_text_raw.strip()
            if cleaned_text.startswith("```json"):
                cleaned_text = cleaned_text[7:]
            if cleaned_text.endswith("```"):
                cleaned_text = cleaned_text[:-3]

            parsed_output = json.loads(cleaned_text)

            required_keys = {"compatibilidade", "pontosFortes", "lacunasIdentificadas", "sugestoesParaEmpresa"}
            if not required_keys.issubset(parsed_output.keys()):
                logger.error(f"Resposta do LLM não contém todas as chaves esperadas. Recebido: {parsed_output.keys()}")
                raise ValueError("Resposta da IA está malformada ou incompleta.")

            logger.info("Avaliação LLM processada com sucesso.")
            return parsed_output 

        except json.JSONDecodeError as je:
            logger.error(f"Falha ao decodificar JSON da resposta do Gemini: {je}. Resposta: {response_text_raw}")
            raise application_exception("Não foi possível interpretar a resposta da IA.", status_code=500)
        except ValueError as ve:
            logger.error(f"Erro de valor ao processar resposta do LLM: {ve}. Resposta: {response_text_raw}")
            raise application_exception(str(ve), status_code=500)

    except application_exception: 
        raise
    except Exception as e:
        logger.exception("Erro inesperado no serviço de avaliação LLM.")
        raise application_exception("Erro interno ao gerar avaliação.", status_code=500)