from flask import Blueprint, request, jsonify
from app.services import resume_nlp_service, compare_to_job_service
from app.exceptions.errors import application_exception, path_not_found

resume_bp = Blueprint('resume', __name__)

@resume_bp.route('/extract_entities', methods=['POST'])
def extract_entities_route():
    data = request.get_json() or {}
    path = data.get('path')

    if not path:
        raise application_exception("Path is required", status_code=400)
    if not isinstance(path, str):
        raise application_exception("Path must be a string", status_code=400)

    try:
        entities = resume_nlp_service.extract_entities(path)
        return jsonify({"entities": entities}), 200
    except application_exception as e:
        raise

    
@resume_bp.route('/compare_resumes', methods=['GET'])
def compare_resumes_route():
    job_id = request.args.get('vaga_id')

    if not job_id:
        raise application_exception("vaga_id is required", status_code=400)

    candidates = compare_to_job_service.search_candidates_from_jobs_service(job_id)
    return jsonify({"candidates": candidates}), 200
