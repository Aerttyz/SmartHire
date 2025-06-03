import { FaseDto } from "@/lib/utils";

export interface Vaga {
  id?: string;
  nome: string;
  isActive: boolean | string;
  requisitos?: {
    idiomas: string;
    habilidades: string;
    formacaoAcademica: string;
    experiencia: string;
    pesoHabilidades: number;
    pesoIdiomas: number;
    pesoFormacaoAcademica: number;
    pesoExperiencia: number;
  };

  empresaNome?: string;
  fase?: FaseDto[];

