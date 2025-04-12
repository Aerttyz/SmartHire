from flask import Flask
from app.routes.resume_nlp_routes import resume_bp

def create_app():
    app = Flask(__name__)
    app.register_blueprint(resume_bp)
    return app

if __name__ == "__main__":
    app = create_app()
    app.run(debug=False)