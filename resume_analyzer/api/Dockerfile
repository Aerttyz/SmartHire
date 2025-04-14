# flask-api/Dockerfile

FROM python:3.12-slim

WORKDIR /backend/src/main/python/api
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
EXPOSE 5000

CMD ["python", "-m", "app.main"]