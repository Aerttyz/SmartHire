package com.smarthire.resume.domain.enums;

public enum Situacao {
    TRIAGEM,
    ANALISE_CURRICULAR,
    DESAFIO_TECNICO,
    ENTREVISTA,
    ADMISSAO;

    public static Situacao[] listSituacao() {return Situacao.values();}
}


