import psycopg2
import psycopg2.extras
from app.config import DB_CONFIG

def get_connection():
    try:
        connection = psycopg2.connect(
            host=DB_CONFIG['host'],
            user=DB_CONFIG['user'],
            password=DB_CONFIG['password'],
            dbname=DB_CONFIG['dbname'],
            port=DB_CONFIG['port']
        )
    except Exception as e:
        print(f"Error connecting to the database: {e}")
        raise
    return connection