import spacy
from app.utils import extract_text_from_pdf
from pathlib import Path
from app.utils.generate_llm_prompts import generate_gemini_prompt
from app.services import gemini_api_service

def extract_entities(path):
    texts = extract_text_from_pdf.open_folder(path)
    model_path = Path("/app/nlp_models/output/model-best")
    nlp = spacy.load(model_path)

    all_resumes = {}
    for  i, text in enumerate(texts):
        doc = nlp(text)
        entities = [(ent.text, ent.label_) for ent in doc.ents]
        clean_text = extract_text_from_pdf.clean_text(text, entities)
        prompt = generate_gemini_prompt(clean_text)
        response = gemini_api_service.gemini_response(prompt)
        all_resumes[i] = {
            "entities": entities,
            "response": response.text
        }
        print(response)
    return all_resumes    
    
