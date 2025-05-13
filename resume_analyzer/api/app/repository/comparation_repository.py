from app.db.connection import get_connection

def search_candidates_from_jobs(job_id):
    connection = get_connection()
    try:
        with connection.cursor() as cursor:
            query = """
                SELECT 
                    c.id, c.nome, c.email, c.telefone, c.situacao,
                    curr.id, curr.nome, curr.email, curr.telefone, curr.experiencia,
                    v.nome, v.is_active,
                    array_remove(array_agg(DISTINCT ci.idiomas), NULL) as idiomas,
                    array_remove(array_agg(DISTINCT ch.habilidades), NULL) as habilidades,
                    array_remove(array_agg(DISTINCT cf.formacao_academica), NULL) as formacoes
                FROM candidato c
                LEFT JOIN curriculo curr ON c.curriculo_id = curr.id
                LEFT JOIN vaga v ON c.vaga_id = v.id
                LEFT JOIN curriculo_idiomas ci ON ci.curriculo_id = curr.id
                LEFT JOIN curriculo_habilidades ch ON ch.curriculo_id = curr.id
                LEFT JOIN curriculo_formacao_academica cf ON cf.curriculo_id = curr.id
                WHERE c.vaga_id = %s
                GROUP BY c.id, curr.id, v.id
            """
            cursor.execute(query, (job_id,))
            rows = cursor.fetchall()
            candidates = []
            for row in rows:
                print(f"Total columns returned: {len(row)}")  # DEBUG: veja quantas colunas existem
                candidato = {
                    'id': row[0],
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
                        'ativa': row[11]
                    }
                }
                candidates.append(candidato)
            return candidates
    except Exception as e:
        print(f"Error fetching candidates: {e}")
        raise
    finally:
        connection.close()