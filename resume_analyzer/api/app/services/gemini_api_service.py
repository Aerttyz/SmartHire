import google.generativeai as genai
from google.api_core.exceptions import InvalidArgument, PermissionDenied, ResourceExhausted
from app.exceptions.errors import application_exception
from app.logger import logger
import os

def gemini_response(prompt):
    try:
        api_key = os.getenv("API_KEY")
        if not api_key:
            raise application_exception("API key is required", status_code=400)
        
        genai.configure(api_key=api_key)
        model = genai.GenerativeModel("gemini-2.0-flash")
        response = model.generate_content(prompt)
        return response
    
    except application_exception as e:
        raise e
    except InvalidArgument as e:
        logger.exception("InvalidArgument when calling Gemini")
        raise application_exception("Unexpected error processing prompt to AI", status_code=400)
    except PermissionDenied as e:
        logger.exception("PermissionDenied when calling Gemini")
        raise application_exception("Denied permission accessing AI service", status_code=403)
    except ResourceExhausted as e:
        logger.exception("ResourceExhausted when calling Gemini")
        raise application_exception("Requests limit to AI service exceeded", status_code=503)
    except Exception as e:
        logger.exception("Unexpected error in gemini_response")
        raise application_exception("Unexpected error using AI service", status_code=500)
