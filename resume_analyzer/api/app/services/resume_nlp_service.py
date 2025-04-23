import spacy
from app.utils import extract_text_from_pdf
from pathlib import Path
from app.utils.generate_llm_prompts import generate_gemini_prompt
from app.services import gemini_api_service
from app.utils import clean_text

def extract_entities(path):
    texts = extract_text_from_pdf.open_folder(path)
    model_path = Path("app/nlp_models/output/model-best")
    
    nlp = spacy.load(model_path)

    all_resumes = {}
    for  i, text in enumerate(texts):
        doc = nlp(text)
        
        entities = [(ent.text, ent.label_) for ent in doc.ents]

        filtered_text = clean_text.remove_stop_words(text)

        cleaned_text = clean_text.remove_entities(filtered_text, entities)

        cleaned_text = clean_text.stemming(cleaned_text)

        prompt = generate_gemini_prompt(cleaned_text)
        response = gemini_api_service.gemini_response(prompt)

        all_resumes[i] = {
            "entities": entities,
            "response": response.text
        }
        print(response)
    return all_resumes    
    
