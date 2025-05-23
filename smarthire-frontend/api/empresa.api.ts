import axios from "axios"

const api = axios.create({
  baseURL: "http://localhost:8080/empresas",
  headers: {
    "Content-Type": "application/json",
  },
})

export interface Empresa {
  id: string;
  nome: string;
  cnpj: string;
  endereco: string;
  telefone: string;
  email: string;
  senha: string;
}

export async function getEmpresas() {
  const response = await api.get<Empresa[]>("/")
  return response.data
}

export async function getEmpresa(nomeEmpresa: string) {
  const response = await api.get<Empresa>(`/${nomeEmpresa}`)
  return response.data
}

export async function createEmpresa(empresa: Omit<Empresa, "id">) {
  const response = await api.post<Empresa>("/", empresa)
  return response.data
}

export async function updateEmpresa(nomeEmpresa: string, empresa: Partial<Empresa>) {
  const response = await api.put<Empresa>(`/${nomeEmpresa}`, empresa)
  return response.data
}

export async function deleteEmpresa(id: string) {
  const response = await api.delete<void>(`/${id}`)
  return response.data
}
