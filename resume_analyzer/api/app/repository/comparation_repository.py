from app.db.connection import get_connection
from app.utils.extract_infos_from_bd import extract_infos_from_bd

def search_candidates_from_jobs(job_id):
    connection = get_connection()
    try:
        with connection.cursor() as cursor:
            query = """
                SELECT 
                    c.id, c.nome, c.email, c.telefone, 
                    curr.id, curr.nome, curr.email, curr.telefone, curr.experiencia,
                    v.nome, v.is_active,
                    
                    array_remove(array_agg(DISTINCT ci.idiomas), NULL) as idiomas,
                    array_remove(array_agg(DISTINCT ch.habilidades), NULL) as habilidades,
                    array_remove(array_agg(DISTINCT cf.formacao_academica), NULL) as formacoes,

                    req.habilidades as requisitos_habilidades,
                    req.idiomas as requisitos_idiomas,
                    req.formacao_academica as requisitos_formacao,
                    req.experiencia as requisitos_experiencia,

                    req.peso_habilidades,
                    req.peso_idiomas,
                    req.peso_formacao_academica,
                    req.peso_experiencia

                FROM candidato c
                LEFT JOIN curriculo curr ON c.curriculo_id = curr.id
                LEFT JOIN vaga v ON c.vaga_id = v.id
                LEFT JOIN curriculo_idiomas ci ON ci.curriculo_id = curr.id
                LEFT JOIN curriculo_habilidades ch ON ch.curriculo_id = curr.id
                LEFT JOIN curriculo_formacao_academica cf ON cf.curriculo_id = curr.id
                LEFT JOIN vaga_requisitos req ON v.id = req.vaga_id

                WHERE c.vaga_id = %s

                GROUP BY 
                    c.id, curr.id, v.id, 
                    req.habilidades, req.idiomas, req.formacao_academica, req.experiencia,
                    req.peso_habilidades, req.peso_idiomas, req.peso_formacao_academica, req.peso_experiencia
            """
            cursor.execute(query, (job_id,))
            rows = cursor.fetchall()
            candidates = []
            for row in rows:
                candidato = {
                    'id': row[0],
                    'nome': row[1],       # utilizado para envio de emails
                    'email': row[2],      # utilizado para envio de emails
                    'curriculo': {
                        'idCurriculo': row[5],
                        'nome': row[6],
                        'email': row[7],
                        'telefone': row[8],
                        'experiencia': row[9],
                        'idiomas': row[12],
                        'habilidades': row[13],
                        'formacaoAcademica': row[14]
                    },
                    'vaga': {
                        'nome': row[10],
                        'ativa': row[11],
                        'requisitos': {
                            'habilidades': row[14],
                            'idiomas': row[15],
                            'formacaoAcademica': row[16],
                            'experiencia': row[17],
                            'pesoHabilidades': row[18],
                            'pesoIdiomas': row[19],
                            'pesoFormacaoAcademica': row[20],
                            'pesoExperiencia': row[21]
                        }
                    }
                }
                candidates.append(candidato)
            return extract_infos_from_bd(candidates)
    except Exception as e:
        print(f"Error fetching candidates: {e}")
        raise
    finally:
        connection.close()