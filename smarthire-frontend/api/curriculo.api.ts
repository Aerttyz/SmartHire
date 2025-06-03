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
