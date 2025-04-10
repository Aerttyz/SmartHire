import fitz # PyMuPDF
from pathlib import Path

def extract_text(path_pdf):
    pdf = fitz.open(path_pdf)
    text = ""
    for page in pdf:
        text += page.get_text()
    return text

def open_folder(path):
    texts = []
    path = Path(path)
    for path_pdf in path.glob("*.pdf"):
        text = extract_text(path_pdf)
        texts.append(text)
    return texts
