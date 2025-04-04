from flask import Flask
from app.extensions import db
from app.config import Config


def create_app():
    app = Flask(__name__)
    app.config.from_object(Config)

    db.init_app(app)

    return app

if __name__ == "__main__":
    app = create_app()
    app.run(debug=True)