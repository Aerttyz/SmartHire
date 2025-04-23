import google.generativeai as genai
from google.api_core.exceptions import InvalidArgument, PermissionDenied, ResourceExhausted

def gemini_response(prompt):
    try:
        api_key = ""
        if not api_key:
            raise ValueError("API key is required")
        
        genai.configure(api_key=api_key)
        model = genai.GenerativeModel("gemini-2.0-flash")
        response = model.generate_content(prompt)
        return response
    
    except ValueError  as e:
        raise ValueError(e)
    except InvalidArgument as e:
        raise InvalidArgument(f"Invalid argument: {e}")
    except PermissionDenied as e:
        raise PermissionDenied(f"Permission denied: {e}")
    except ResourceExhausted as e:
        raise ResourceExhausted(f"Resource exhausted: {e}")