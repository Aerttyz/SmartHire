import spacy
import nltk
from nltk.stem import RSLPStemmer
from app.exceptions import errors

def remove_entities(text, entities):
    clean_text = text

    for entity in entities:
        clean_text = clean_text.replace(entity[0], "")
        
    return clean_text.strip()

def remove_stop_words(text):
    try:
        nlp = spacy.load("pt_core_news_sm")
        doc = nlp(text)
    except OSError as e:
        raise errors.model_not_found(f"Model not found: pt_core_news_sm") from e
    

    filtered_words = [token.text for token in doc if not token.is_stop and not token.is_punct]
    filtered_text = " ".join(filtered_words)

    return filtered_text.strip()

nltk.download('punkt')
  
def stemming(text):
    
    try:
        stemmer = RSLPStemmer()
        tokens = nltk.word_tokenize(text, language='portuguese')
        stemmed_tokens = [stemmer.stem(token) for token in tokens]
        stemmed_text = ' '.join(stemmed_tokens)
    except Exception as e:
        raise RuntimeError(f"Ocorreu um erro ao processar o stemming: {str(e)}")
    
    return stemmed_text.strip()