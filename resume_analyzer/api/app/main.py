from flask import Flask
from app.routes.resume_nlp_routes import resume_bp
from app.exceptions.handlers import register_error_handlers


def create_app():
    app = Flask(__name__)
    app.register_blueprint(resume_bp)
    register_error_handlers(app)
    return app

if __name__ == "__main__":
    app = create_app()
    app.run(host="0.0.0.0", port=5000,debug=False)