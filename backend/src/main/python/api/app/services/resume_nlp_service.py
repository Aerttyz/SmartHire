import spacy
from app.utils import extract_text_from_pdf
from pathlib import Path

def extract_entities(path):
    texts = extract_text_from_pdf.open_folder(path)
    model_path = Path(__file__).parent.parent / "nlp_models" / "output" / "model-best"
    nlp = spacy.load(model_path)

    all_entities = []
    for text in texts:
        doc = nlp(text)
        entities = [(ent.text, ent.label_) for ent in doc.ents]
        all_entities.append(entities)
    return all_entities    
    
