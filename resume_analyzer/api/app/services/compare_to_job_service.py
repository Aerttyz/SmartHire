from app.utils.generate_llm_prompts import generate_gemini_prompt_to_comparation
from app.repository.comparation_repository import search_candidates_from_jobs
from app.exceptions.errors import application_exception
from app.logger import logger

def search_candidates_from_jobs_service(job_id):
    try:
        candidates = search_candidates_from_jobs(job_id)
        response = generate_gemini_prompt_to_comparation(candidates)
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

