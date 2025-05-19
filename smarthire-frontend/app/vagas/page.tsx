"use client"

import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"

export default function VagasPage() {
  const sidebarItems = [
    { id: "adicionar", label: "Adicionar uma vaga" },
    { id: "listar", label: "Listar vagas cadastradas" },
    { id: "atualizar", label: "Atualizar dados de vaga" },
    { id: "apagar", label: "Apagar dados de vaga" },
  ]

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
              { name: "empresa", label: "Empresa", type: "text" },
              { name: "descricao", label: "Descrição", type: "textarea" },
              { name: "requisitos", label: "Requisitos", type: "textarea" },
              { name: "salario", label: "Salário", type: "text" },
              { name: "local", label: "Local", type: "text" },
            ]}
            submitLabel="Adicionar Vaga"
            onSubmit={(data) => console.log("Adicionar vaga:", data)}
          />
          <CrudSection
            id="listar"
            title="Listar vagas cadastradas"
            description="Visualize todas as vagas cadastradas na plataforma."
            fields={[{ name: "busca", label: "Buscar por título", type: "text" }]}
            submitLabel="Buscar"
            onSubmit={(data) => console.log("Buscar vagas:", data)}
            showTable={true}
            tableHeaders={["Título", "Empresa", "Local", "Salário", "Ações"]}
            tableData={[
              ["Desenvolvedor Frontend", "Empresa A", "São Paulo, SP", "R$ 8.000,00", ""],
              ["Desenvolvedor Backend", "Empresa B", "Rio de Janeiro, RJ", "R$ 9.000,00", ""],
            ]}
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
