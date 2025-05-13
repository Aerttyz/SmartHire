from app.utils.generate_llm_prompts import generate_gemini_prompt_to_comparation
from app.repository.comparation_repository import search_candidates_from_jobs

def search_candidates_from_jobs_service(job_id):
    try:
        candidates = search_candidates_from_jobs(job_id)
        response = generate_gemini_prompt_to_comparation(candidates)
        return response
    except Exception as e:
        print(f"Error in service: {e}")
        raise