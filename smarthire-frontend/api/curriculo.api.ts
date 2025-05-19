import axios from "axios"

const api = axios.create({
  baseURL: "/api/curriculos",
  headers: {
    "Content-Type": "application/json",
  },
})

export interface Curriculo {
  id: string
  nome: string
  email: string
  telefone: string
  formacao: string
  experiencia: string
  habilidades: string
}

export async function getCurriculos() {
  const response = await api.get<Curriculo[]>("/")
  return response.data
}

export async function getCurriculo(id: string) {
  const response = await api.get<Curriculo>(`/${id}`)
  return response.data
}

export async function createCurriculo(curriculo: Omit<Curriculo, "id">) {
  const response = await api.post<Curriculo>("/", curriculo)
  return response.data
}

export async function updateCurriculo(id: string, curriculo: Partial<Curriculo>) {
  const response = await api.put<Curriculo>(`/${id}`, curriculo)
  return response.data
}

export async function deleteCurriculo(id: string) {
  const response = await api.delete<void>(`/${id}`)
  return response.data
}
