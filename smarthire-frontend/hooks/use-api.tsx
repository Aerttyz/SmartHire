"use client"

import axios from "axios"
import { useState } from "react"

interface ApiState<T> {
  data: T | null
  loading: boolean
  error: Error | null
}

export function useApi<T>() {
  const [state, setState] = useState<ApiState<T>>({
    data: null,
    loading: false,
    error: null,
  })

  const api = axios.create({
    baseURL: "http://localhost:8080/empresas",
    headers: {
      "Content-Type": "application/json",
    },
  })

  const fetchData = async (url: string) => {
    setState({ ...state, loading: true })
    try {
      const response = await api.get<T>(url)
      setState({ data: response.data, loading: false, error: null })
      return response.data
    } catch (error) {
      setState({ data: null, loading: false, error: error as Error })
      throw error
    }
  }

  const postData = async (url: string, data: any) => {
    setState({ ...state, loading: true })
    try {
      const response = await api.post<T>(url, data)
      setState({ data: response.data, loading: false, error: null })
      return response.data
    } catch (error) {
      setState({ data: null, loading: false, error: error as Error })
      throw error
    }
  }

  const updateData = async (url: string, data: any) => {
    setState({ ...state, loading: true })
    try {
      const response = await api.put<T>(url, data)
      setState({ data: response.data, loading: false, error: null })
      return response.data
    } catch (error) {
      setState({ data: null, loading: false, error: error as Error })
      throw error
    }
  }

  const deleteData = async (url: string) => {
    setState({ ...state, loading: true })
    try {
      const response = await api.delete<T>(url)
      setState({ data: response.data, loading: false, error: null })
      return response.data
    } catch (error) {
      setState({ data: null, loading: false, error: error as Error })
      throw error
    }
  }

  return {
    ...state,
    fetchData,
    postData,
    updateData,
    deleteData,
  }
}
