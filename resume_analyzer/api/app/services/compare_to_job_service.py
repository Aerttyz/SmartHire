from app.utils.generate_llm_prompts import generate_gemini_prompt_to_comparation
from app.exceptions.errors import application_exception
from app.logger import logger

def search_candidates_from_jobs_service(prompts):
    try:
        response = generate_gemini_prompt_to_comparation(prompts)
        return response
    except ValueError as e:
        logger.exception("ValueError during candidate comparison ")
        raise application_exception("Invalid data when comparing candidates", status_code=400)
    except TypeError as e:
        logger.exception("TypeError during candidate comparison")
        raise application_exception("Internal error during comparison process", status_code=500)
    except Exception as e:
        logger.exception("Unexpected error during candidate comparison")
        raise application_exception("Unexpected error while processing candidate comparison", status_code=500)

