"use client"

import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"

export default function CurriculosPage() {
  const sidebarItems = [
    { id: "adicionar", label: "Adicionar um currículo" },
    { id: "listar", label: "Listar currículos cadastrados" },
    { id: "atualizar", label: "Atualizar dados de currículo" },
    { id: "apagar", label: "Apagar currículo" },
  ]

  return (
    <DashboardShell>
      <DashboardHeader heading="Gerenciamento de Currículos" text="Analise currículos com inteligência artificial." />
      <div className="flex flex-col gap-8 md:flex-row">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <CrudSection
            id="adicionar"
            title="Adicionar um currículo"
            description="Preencha os campos abaixo para adicionar um novo currículo."
            fields={[
              { name: "nome", label: "Nome do Candidato", type: "text" },
              { name: "email", label: "Email", type: "email" },
              { name: "telefone", label: "Telefone", type: "text" },
              { name: "formacao", label: "Formação Acadêmica", type: "textarea" },
              { name: "experiencia", label: "Experiência Profissional", type: "textarea" },
              { name: "habilidades", label: "Habilidades", type: "textarea" },
              { name: "arquivo", label: "Arquivo do Currículo", type: "file" },
            ]}
            submitLabel="Adicionar Currículo"
            onSubmit={(data) => console.log("Adicionar currículo:", data)}
          />
          <CrudSection
            id="listar"
            title="Listar currículos cadastrados"
            description="Visualize todos os currículos cadastrados na plataforma."
            fields={[{ name: "busca", label: "Buscar por nome", type: "text" }]}
            submitLabel="Buscar"
            onSubmit={(data) => console.log("Buscar currículos:", data)}
            showTable={true}
            tableHeaders={["Nome", "Email", "Telefone", "Formação", "Ações"]}
            tableData={[
              ["João Silva", "joao.silva@email.com", "(11) 1111-1111", "Ciência da Computação", ""],
              ["Maria Santos", "maria.santos@email.com", "(22) 2222-2222", "Engenharia de Software", ""],
            ]}
          />
          <CrudSection
            id="atualizar"
            title="Atualizar dados de currículo"
            description="Selecione um currículo e atualize seus dados."
            fields={[
              { name: "id", label: "ID do Currículo", type: "text" },
              { name: "nome", label: "Nome do Candidato", type: "text" },
              { name: "email", label: "Email", type: "email" },
              { name: "telefone", label: "Telefone", type: "text" },
              { name: "formacao", label: "Formação Acadêmica", type: "textarea" },
              { name: "experiencia", label: "Experiência Profissional", type: "textarea" },
              { name: "habilidades", label: "Habilidades", type: "textarea" },
            ]}
            submitLabel="Atualizar Currículo"
            onSubmit={(data) => console.log("Atualizar currículo:", data)}
          />
          <CrudSection
            id="apagar"
            title="Apagar currículo"
            description="Selecione um currículo para remover do sistema."
            fields={[{ name: "id", label: "ID do Currículo", type: "text" }]}
            submitLabel="Apagar Currículo"
            onSubmit={(data) => console.log("Apagar currículo:", data)}
            isDanger={true}
          />
        </div>
      </div>
    </DashboardShell>
  )
}
