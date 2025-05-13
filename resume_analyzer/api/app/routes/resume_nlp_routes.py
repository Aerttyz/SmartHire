from flask import Blueprint, request, jsonify
from app.services import resume_nlp_service, compare_to_job_service
from app.exceptions import errors
from google.api_core.exceptions import InvalidArgument, PermissionDenied, ResourceExhausted

resume_bp = Blueprint('resume', __name__)

@resume_bp.route('/extract_entities', methods=['POST'])
def extract_entities_route():
    path = request.get_json().get('path')
    if not path:
        return jsonify({"error": "Path is required"}), 400
    try:
        entities = resume_nlp_service.extract_entities(path)
        return jsonify({"entities": entities}), 200
    except errors.model_not_found as e:
        return jsonify({"error": str(e)}), 404
    except errors.path_not_found as e:
        return jsonify({"error": str(e)}), 404
    except errors.file_not_found as e:
        return jsonify({"error": str(e)}), 404
    except InvalidArgument as e:
        return jsonify({"error": f"Invalid argument: {e}"}), 400
    except PermissionDenied as e:   
        return jsonify({"error": f"Permission denied: {e}"}), 403
    except ResourceExhausted as e:
        return jsonify({"error": f"Resource exhausted: {e}"}), 503
    except Exception as e:
        return jsonify({"error": f"An unexpected error occurred: {str(e)}"}), 500
    
@resume_bp.route('/compare_resumes', methods=['GET'])
def compare_resumes_route():
    job_id = request.args.get('vaga_id')

    if not job_id:
        return jsonify({"error": "vaga_id is required"}), 400
    
    candidates = compare_to_job_service.search_candidates_from_jobs_service(job_id)
    return jsonify({"candidates": candidates}), 200