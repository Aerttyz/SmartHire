# flask-api/Dockerfile

# Usar uma imagem base do Python
FROM python:3.12-slim

# Instalar o wget para descarregar o nosso modelo
RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o ficheiro de requerimentos primeiro para aproveitar o cache do Docker
COPY requirements.txt .

# Instalar as dependências do Python
RUN pip install --no-cache-dir -r requirements.txt

# Descarregar os modelos de linguagem do Spacy e NLTK
RUN python -m spacy download pt_core_news_sm
RUN python -c "import nltk; nltk.download('punkt')"

# --- Download e extração do modelo grande ---
# Substitua a URL abaixo pelo seu link de download direto
ARG MODEL_URL="https://drive.google.com/uc?export=download&id=1zfjbvXV4SZm4dhPOVjHcWFKkkC0AW1NG"

# Descarregar e descompactar o modelo nlp_models
RUN wget -O nlp_models.zip "${MODEL_URL}" && \
    unzip nlp_models.zip -d . && \
    rm nlp_models.zip

# Copiar o restante do código da aplicação
COPY . .

# Expor a porta que a aplicação Flask usa
EXPOSE 5000

# Comando para executar a aplicação
CMD ["python", "-m", "app.main"]