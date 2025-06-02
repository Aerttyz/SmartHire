import { FaseDto } from "@/lib/utils";
import axios from "axios";

const api = axios.create({
  baseURL: "/api/vagas",
  headers: {
    "Content-Type": "application/json",
  },
});

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
}

export async function getVagas() {
  const response = await api.get<Vaga[]>("/");
  return response.data;
}

export async function getVaga(id: string) {
  const response = await api.get<Vaga>(`/${id}`);
  return response.data;
}

export async function createVaga(vaga: Omit<Vaga, "id">) {
  const response = await api.post<Vaga>("/", vaga);
  return response.data;
}

export async function updateVaga(id: string, vaga: Partial<Vaga>) {
  const response = await api.put<Vaga>(`/${id}`, vaga);
  return response.data;
}

export async function deleteVaga(id: string) {
  const response = await api.delete<void>(`/${id}`);
  return response.data;
}
