from flask import Blueprint, request, jsonify
from app.services import resume_nlp_service, compare_to_job_service
from app.exceptions.errors import application_exception, path_not_found
from app.services import avaliacao_service

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

    
@resume_bp.route('/compare_resumes', methods=['POST'])
def compare_resumes_route():
    prompts = request.get_json() 

    candidates = compare_to_job_service.search_candidates_from_jobs_service(prompts)
    return jsonify({"candidates": candidates}), 200


@resume_bp.route('/avaliar', methods=['POST'])
def avaliar_compatibilidade_route():
    data = request.get_json()
    if not data or 'curriculo' not in data or 'vaga' not in data:
        raise application_exception("Payload inválido. 'curriculo' e 'vaga' são obrigatórios.", status_code=400)
    
    curriculo_info = data.get('curriculo')
    vaga_info = data.get('vaga')

    try:
        resultado_avaliacao = avaliacao_service.gerar_avaliacao_detalhada_llm(curriculo_info, vaga_info)
        return jsonify(resultado_avaliacao), 200
    except Exception as e:
        raise 