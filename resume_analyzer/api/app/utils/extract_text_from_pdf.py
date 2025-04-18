import fitz # PyMuPDF
from pathlib import Path
from app.exceptions import errors

def extract_text(path_pdf):
    try:
        pdf = fitz.open(path_pdf)
        text = ""
        for page in pdf:
            text += page.get_text()
        return text
    except errors.file_not_found as e:
        raise errors.errosfile_not_found(f"File not found: {path_pdf}") from e

def open_folder(path):
    path = Path(path)

    if not path.exists():
        raise errors.path_not_found(f"Path not found: {path}")
    
    texts = []
    pdf_files = list(path.glob("*.pdf"))

    if not pdf_files:
        raise errors.file_not_found(f"No PDF files found in the directory: {path}")

    for path_pdf in pdf_files: 
       if path_pdf.is_file():
            text = extract_text(path_pdf)
            texts.append(text)
            
    return texts
    
def clean_text(text, entities):
    clean_text = text

    for entity in entities:
        clean_text = clean_text.replace(entity[0], "")
        
    return clean_text.strip()
