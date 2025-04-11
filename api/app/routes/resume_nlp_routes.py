from flask import Blueprint, request, jsonify
from app.services import resume_nlp_service
from app.exceptions import errors

resume_bp = Blueprint('resume', __name__)

@resume_bp.route('/extract_entities', methods=['POST'])
def extract_entities_route():
    path = request.get_json().get('path')
    if not path:
        return jsonify({"error": "Path is required"}), 400
    try:
        entities = resume_nlp_service.extract_entities(path)
        return jsonify({"entities": entities}), 200
    except errors.path_not_found as e:
        return jsonify({"error": str(e)}), 404
    except errors.file_not_found as e:
        return jsonify({"error": str(e)}), 404
