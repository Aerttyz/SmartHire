"use client"

import { Vaga } from "@/api/vaga.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { useEffect, useState } from "react"

export default function VagasPage() {
    const [vagas, setVagas] = useState<Vaga[]>([]);
    const [vagaBuscada, setVagaBuscada] = useState<Vaga | null>(null);
    const API_URL = "http://localhost:8080/vagas";

  const sidebarItems = [
    { id: "adicionar", label: "Adicionar uma vaga" },
    { id: "listar", label: "Listar vagas cadastradas" },
    { id: "atualizar", label: "Atualizar dados de vaga" },
    { id: "apagar", label: "Apagar dados de vaga" },
  ]

  
    useEffect(() => {
      fetch("http://localhost:8080/vagas")
        .then((res) => res.json())
        .then((data: Vaga[]) => {
          setVagas(data);
        })
        .catch((error) => {
          console.error("Erro ao buscar vagas: ", error)
        })
    }, []);

  async function adicionarVaga(data: any) {
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
      const result = await response.json()
      console.log("Vaga adicionada:", result)
      alert("Vaga cadastrada com sucesso!");
    } catch (error) {
      console.error("Erro ao cadastrar vaga:", error)
      alert(`Erro ao cadastrar vaga: ${error}`);
    }
  }

  return (
    <DashboardShell>
      <DashboardHeader heading="Gerenciamento de Vagas" text="Crie e gerencie vagas de emprego." />
      <div className="flex flex-col gap-8 md:flex-row">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <CrudSection
            id="adicionar"
            title="Adicionar uma vaga"
            description="Preencha os campos abaixo para adicionar uma nova vaga."
            fields={[
              { name: "titulo", label: "Título da Vaga", type: "text" },
              { name: "empresaId", label: "ID Empresa", type: "text" },
              { name: "habilidades", label: "Habilidades", type: "textarea" },
              { name: "idiomas", label: "Idiomas", type: "text" },
              { name: "formacaoAcademica", label: "Nível (Médio completo, Graduação, Pós-graduação, etc)", type: "text" },
              { name: "experiencia", label: "Tempo de experiência", type: "text" },

              { name: "pesoHabilidades", label: "Peso HABILIDADES", type: "number" },
              { name: "pesoIdiomas", label: "Peso IDIOMAS", type: "number" },
              { name: "pesoFormacao", label: "Peso FORMAÇÃO ACADÊMICA", type: "number" },
              { name: "pesoExperiencia", label: "Peso EXPERIÊNCIA", type: "number" },
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
            onSubmit={(data) => console.log("Buscar vagas:", data)}
            showTable={true}
            tableHeaders={[]}
            tableData={[]}
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
            onSubmit={(data) => console.log("Atualizar vaga:", data)}
          />
          <CrudSection
            id="apagar"
            title="Apagar dados de vaga"
            description="Selecione uma vaga para remover do sistema."
            fields={[{ name: "id", label: "ID da Vaga", type: "text" }]}
            submitLabel="Apagar Vaga"
            onSubmit={(data) => console.log("Apagar vaga:", data)}
            isDanger={true}
          />
        </div>
      </div>
    </DashboardShell>
  )
}
