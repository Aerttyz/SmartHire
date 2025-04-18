import google.generativeai as genai

def gemini_response(prompt):
    genai.configure(api_key="")
    model = genai.GenerativeModel("gemini-2.0-flash")
    response = model.generate_content(prompt)
    return response