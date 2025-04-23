import spacy
import nltk
from nltk.stem import RSLPStemmer


def remove_entities(text, entities):
    clean_text = text

    for entity in entities:
        clean_text = clean_text.replace(entity[0], "")
        
    return clean_text.strip()

def remove_stop_words(text):
    
    nlp = spacy.load("pt_core_news_sm")
    doc = nlp(text)

    filtered_words = [token.text for token in doc if not token.is_stop and not token.is_punct]
    filtered_text = " ".join(filtered_words)

    return filtered_text.strip()
    
def stemming(text):
    stemmer = RSLPStemmer()
    tokens = nltk.word_tokenize(text, language='portuguese')
    stemmed_tokens = [stemmer.stem(token) for token in tokens]
    stemmed_text = ' '.join(stemmed_tokens)
    
    return stemmed_text.strip()