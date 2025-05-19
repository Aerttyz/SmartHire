"use client"

import { Vaga } from "@/api/vaga.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { useEffect, useState } from "react"

export default function VagasPage() {
  const [vagas, setVagas] = useState<Vaga[]>([])
  const [vagaBuscada, setVagaBuscada] = useState<Vaga | null>(null)

  const sidebarItems = [
    { id: "adicionar", label: "Adicionar uma vaga" },
    { id: "listar", label: "Listar vagas cadastradas" },
    { id: "atualizar", label: "Atualizar dados de vaga" },
    { id: "apagar", label: "Apagar dados de vaga" },
  ]

  const API_URL = "http://localhost:8080/vagas"

  useEffect(() => {
    fetch(API_URL)
      .then(res => res.json())
      .then((data: Vaga[]) => setVagas(data))
      .catch(err => console.error("Erro ao buscar vagas:", err))
  }, [])

  async function adicionarVaga(data: any) {
    try {
      const res = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
      const result = await res.json()
      console.log("Vaga adicionada:", result)
    } catch (error) {
      console.error("Erro ao adicionar vaga:", error)
    }
  }

  async function buscarVaga(data: any) {
    try {
      const res = await fetch(`${API_URL}/${data.busca}`)
      const result = await res.json()
      setVagaBuscada(result)
      console.log("Vaga encontrada:", result)
    } catch (error) {
      console.error("Erro ao buscar vaga:", error)
      setVagaBuscada(null)
    }
  }

  async function atualizarVaga(data: any) {
    try {
      const res = await fetch(`${API_URL}/${data.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
      const result = await res.json()
      console.log("Vaga atualizada:", result)
    } catch (error) {
      console.error("Erro ao atualizar vaga:", error)
    }
  }

  async function apagarVaga(data: any) {
    try {
      const res = await fetch(`${API_URL}/${data.id}`, {
        method: "DELETE",
      })
      if (res.ok) {
        console.log("Vaga removida com sucesso")
      } else {
        console.error("Erro ao remover vaga")
      }
    } catch (error) {
      console.error("Erro ao apagar vaga:", error)
    }
  }

  return (
    <DashboardShell>
      <DashboardHeader heading="Gerenciamento de Vagas" text="Crie e gerencie vagas de emprego." />
      <div className="flex flex-col gap-8 md:flex-row py-10">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <CrudSection
            id="adicionar"
            title="Adicionar uma vaga"
            description="Preencha os campos abaixo para adicionar uma nova vaga."
            fields={[
              { name: "nome", label: "Título da Vaga", type: "text" },
              { name: "descricao", label: "Descrição", type: "textarea" },
              { name: "isActive", label: "Vaga Ativa?", type: "checkbox" },
              { name: "nomeEmpresa", label: "Empresa", type: "text" },
              { name: "requisitos", label: "Requisitos", type: "text" },
            ]}
            submitLabel="Adicionar Vaga"
            onSubmit={adicionarVaga}
          />
          <CrudSection
            id="listar"
            title="Listar vagas cadastradas"
            description="Visualize todas as vagas cadastradas na plataforma."
            fields={[{ name: "busca", label: "Buscar por título", type: "text" }]}
            submitLabel="Buscar"
            onSubmit={buscarVaga}
            showTable={true}
            tableHeaders={["Título", "Empresa", "Local", "Salário"]}
            tableData={
              vagaBuscada
                ? [[
                    vagaBuscada.titulo,
                    vagaBuscada.empresa,
                    vagaBuscada.local,
                    vagaBuscada.salario,
                  ]]
                : vagas.map((vaga) => [
                    vaga.titulo,
                    vaga.empresa,
                    vaga.local,
                    vaga.salario,
                  ])
            }
          />
          <CrudSection
            id="atualizar"
            title="Atualizar dados de vaga"
            description="Selecione uma vaga e atualize seus dados."
            fields={[
              { name: "id", label: "ID da Vaga", type: "text" },
              { name: "titulo", label: "Título da Vaga", type: "text" },
              { name: "empresa", label: "Empresa", type: "text" },
              { name: "descricao", label: "Descrição", type: "textarea" },
              { name: "requisitos", label: "Requisitos", type: "textarea" },
              { name: "salario", label: "Salário", type: "text" },
              { name: "local", label: "Local", type: "text" },
            ]}
            submitLabel="Atualizar Vaga"
            onSubmit={atualizarVaga}
          />
          <CrudSection
            id="apagar"
            title="Apagar dados de vaga"
            description="Selecione uma vaga para remover do sistema."
            fields={[{ name: "id", label: "ID da Vaga", type: "text" }]}
            submitLabel="Apagar Vaga"
            onSubmit={apagarVaga}
            isDanger={true}
          />
        </div>
      </div>
    </DashboardShell>
  )
}
