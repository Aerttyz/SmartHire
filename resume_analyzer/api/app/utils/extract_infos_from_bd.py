def extract_infos_from_bd(data):
    first_candidate = data[0]

    job_requirements = first_candidate['vaga']['requisitos']['habilidades']
    job_experience = first_candidate['vaga']['requisitos']['experiencia']
    job_languages = first_candidate['vaga']['requisitos']['idiomas']
    job_academic_background = first_candidate['vaga']['requisitos']['formacaoAcademica']

    job_requirements_weight = first_candidate['vaga']['requisitos']['pesoHabilidades']
    job_experience_weight = first_candidate['vaga']['requisitos']['pesoExperiencia']
    job_languages_weight = first_candidate['vaga']['requisitos']['pesoIdiomas']
    job_academic_background_weight = first_candidate['vaga']['requisitos']['pesoFormacaoAcademica']

    candidatos_info = []

    for candidato in data:
        curriculo = candidato['curriculo']

        candidato_info = {
            "id": candidato.get("id"),
            "idCurriculo": curriculo.get("idCurriculo"),
            "nome": curriculo.get("nome"),
            "email": curriculo.get("email"),
            "comparacao": {
                "habilidades": {
                    "candidato": curriculo.get("habilidades", []),
                    "vaga": job_requirements,
                    "peso": job_requirements_weight
                },
                "experiencia": {
                    "candidato": curriculo.get("experiencia"),
                    "vaga": job_experience,
                    "peso": job_experience_weight
                },
                "idiomas": {
                    "candidato": curriculo.get("idiomas", []),
                    "vaga": job_languages,
                    "peso": job_languages_weight
                },
                "formacaoAcademica": {
                    "candidato": curriculo.get("formacaoAcademica", []),
                    "vaga": job_academic_background,
                    "peso": job_academic_background_weight
                }
            }
        }

        candidatos_info.append(candidato_info)
    return candidatos_info

