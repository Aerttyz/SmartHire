from flask import jsonify
from app.logger import logger
from app.exceptions import errors
from google.api_core.exceptions import InvalidArgument, PermissionDenied, ResourceExhausted

def register_error_handlers(app):
    @app.errorhandler(errors.application_exception)
    def handle_application_exception(e):
        logger.exception(f"Application exception: {e.message}")
        response = jsonify(e.to_dict())
        response.status_code = e.status_code
        return response

    @app.errorhandler(InvalidArgument)
    def handle_invalid_argument(e):
        logger.exception(f"Invalid argument: {str(e)}")
        response = jsonify({
            "error": "Invalid request parameters",
            "status": 400
        })
        response.status_code = 400
        return response

    @app.errorhandler(PermissionDenied)
    def handle_permission_denied(e):
        logger.exception(f"Permission denied: {str(e)}")
        response = jsonify({
            "error": "Access denied to resource",
            "status": 403
        })
        response.status_code = 403
        return response

    @app.errorhandler(ResourceExhausted)
    def handle_resource_exhausted(e):
        logger.exception(f"Resource exhausted: {str(e)}")
        response = jsonify({
            "error": "Service quota exceeded",
            "status": 503
        })
        response.status_code = 503
        return response

    @app.errorhandler(404)
    def handle_not_found(e):
        logger.warning(f"Route not found: {str(e)}")
        response = jsonify({
            "error": "Resource not found",
            "status": 404
        })
        response.status_code = 404
        return response
    
    @app.errorhandler(405)
    def handle_method_not_allowed(e):
        logger.warning(f"Method not allowed: {str(e)}")
        return jsonify({
            "error": "HTTP method not allowed for this resource",
            "status": 405
        }), 405
    
    @app.errorhandler(Exception)
    def handle_generic_exception(e):
        logger.exception(f"Unhandled exception: {str(e)}")
        response = jsonify({
            "error": "An unexpected error occurred",
            "status": 500
        })
        response.status_code = 500
        return response