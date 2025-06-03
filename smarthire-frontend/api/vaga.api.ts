import { FaseDto } from "@/lib/utils";

export interface Vaga {
  id?: string;
  nome: string;
  isActive: boolean | string;
  habilidades: string;
  idiomas: string;
  formacaoAcademica: string;
  experiencia: string;
  pesoHabilidades: number;
  pesoIdiomas: number;
  pesoFormacaoAcademica: number;
  pesoExperiencia: number;

  empresaNome?: string;
  fase?: FaseDto[];
}