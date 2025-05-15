"use client"

import { Empresa } from "@/api/empresa.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { useEffect, useState } from "react"

export default function AdminPage() {
  const [empresas, setEmpresas] = useState<Empresa[]>([]);

  const sidebarItems = [
    { id: "adicionar", label: "Adicionar uma empresa" },
    { id: "listar", label: "Listar empresas cadastradas" },
    { id: "atualizar", label: "Atualizar dados de empresa" },
    { id: "apagar", label: "Apagar dados de empresa" },
  ]

  const API_URL = "http://localhost:8080/empresas";

  useEffect(() => {
    fetch("http://localhost:8080/empresas")
      .then((res) => res.json())
      .then((data: Empresa[]) => {
        setEmpresas(data);
      })
      .catch((error) => {
        console.error("Erro ao buscar empresas: ", error)
      })
  }, []);


  async function adicionarEmpresa(data: any) {
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
      const result = await response.json()
      console.log("Empresa adicionada:", result)
    } catch (error) {
      console.error("Erro ao adicionar empresa:", error)
    }
  }

  // GET - Listar (por nome)
  async function listarEmpresas(data: any) {
    try {
      const response = await fetch(`${API_URL}/${data.busca}`)
      const result = await response.json()
      console.log("Empresa encontrada:", result)
    } catch (error) {
      console.error("Erro ao buscar empresa:", error)
    }
  }

  // PUT - Atualizar
  async function atualizarEmpresa(data: any) {
    try {
      const response = await fetch(`${API_URL}/${data.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
      const result = await response.json()
      console.log("Empresa atualizada:", result)
    } catch (error) {
      console.error("Erro ao atualizar empresa:", error)
    }
  }

  // DELETE - Remover
  async function apagarEmpresa(data: any) {
    try {
      const response = await fetch(`${API_URL}/${data.id}`, {
        method: "DELETE",
      })
      if (response.ok) {
        console.log("Empresa removida com sucesso")
      } else {
        console.error("Erro ao remover empresa")
      }
    } catch (error) {
      console.error("Erro ao apagar empresa:", error)
    }
  }

  return (
    <DashboardShell>
      <DashboardHeader heading="Área do Administrador" text="Gerencie as empresas cadastradas na plataforma." />
      <div className="flex flex-col gap-8 md:flex-row">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <CrudSection
            id="adicionar"
            title="Adicionar uma empresa"
            description="Preencha os campos abaixo para adicionar uma nova empresa."
            fields={[
              { name: "nome", label: "Nome da Empresa", type: "text" },
              { name: "cnpj", label: "CNPJ", type: "text" },
              { name: "endereco", label: "Endereço", type: "text" },
              { name: "telefone", label: "Telefone", type: "text" },
              { name: "email", label: "Email", type: "email" },
            ]}
            submitLabel="Adicionar Empresa"
            onSubmit={adicionarEmpresa}
          />
          <CrudSection
            id="listar"
            title="Listar empresas cadastradas"
            description="Visualize todas as empresas cadastradas na plataforma."
            fields={[{ name: "busca", label: "Buscar por nome", type: "text" }]}
            submitLabel="Buscar"
            onSubmit={listarEmpresas}
            showTable={false} // Desativei a tabela por enquanto
            tableHeaders={[]}
            tableData={[]}
          />

 
              <CrudSection
                id="listar"
                title="Listar empresas cadastradas"
                description="Visualize todas as empresas cadastradas na plataforma."
                fields={[{ name: "busca", label: "Buscar por nome", type: "text" }]}
                submitLabel="Buscar"
                onSubmit={(data) => console.log("Buscar empresas:", data)}
                showTable={true}
                tableHeaders={["Nome", "CNPJ", "Telefone", "Email"]}
                tableData={empresas.map((empresa) => [
                  empresa.nome,
                  empresa.cnpj,
                  empresa.telefone,
                  empresa.email,
                ])}
              />
  


          <CrudSection
            id="atualizar"
            title="Atualizar dados de empresa"
            description="Selecione uma empresa e atualize seus dados."
            fields={[
              { name: "id", label: "ID da Empresa", type: "text" },
              { name: "nome", label: "Nome da Empresa", type: "text" },
              { name: "cnpj", label: "CNPJ", type: "text" },
              { name: "endereco", label: "Endereço", type: "text" },
              { name: "telefone", label: "Telefone", type: "text" },
              { name: "email", label: "Email", type: "email" },
            ]}
            submitLabel="Atualizar Empresa"
            onSubmit={atualizarEmpresa}
          />
          <CrudSection
            id="apagar"
            title="Apagar dados de empresa"
            description="Selecione uma empresa para remover do sistema."
            fields={[{ name: "id", label: "ID da Empresa", type: "text" }]}
            submitLabel="Apagar Empresa"
            onSubmit={apagarEmpresa}
            isDanger={true}
          />
        </div>
      </div>
    </DashboardShell>
  )
}
