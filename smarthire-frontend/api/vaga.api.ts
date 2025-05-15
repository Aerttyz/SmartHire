import axios from "axios"

const api = axios.create({
  baseURL: "/api/vagas",
  headers: {
    "Content-Type": "application/json",
  },
})

export interface Vaga {
  id: string
  titulo: string
  empresa: string
  descricao: string
  requisitos: string
  salario: string
  local: string
}

export async function getVagas() {
  const response = await api.get<Vaga[]>("/")
  return response.data
}

export async function getVaga(id: string) {
  const response = await api.get<Vaga>(`/${id}`)
  return response.data
}

export async function createVaga(vaga: Omit<Vaga, "id">) {
  const response = await api.post<Vaga>("/", vaga)
  return response.data
}

export async function updateVaga(id: string, vaga: Partial<Vaga>) {
  const response = await api.put<Vaga>(`/${id}`, vaga)
  return response.data
}

export async function deleteVaga(id: string) {
  const response = await api.delete<void>(`/${id}`)
  return response.data
}
