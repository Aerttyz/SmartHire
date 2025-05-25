"use client"

import { Vaga } from "@/api/vaga.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { useEffect, useState } from "react"

export default function VagasPage() {
  
  const [vagaBuscada, setVagaBuscada] = useState<Vaga | null>(null);
  const [vagas, setVagas] = useState<Vaga[]>([]);
  const [vaga, setVaga] = useState<Vaga>({
    nome: "",
    isActive: true,
    habilidades: "",
    idiomas: "",
    formacaoAcademica: "",
    experiencia: "",
    pesoHabilidades: 1,
    pesoIdiomas: 1,
    pesoFormacaoAcademica: 1,
    pesoExperiencia: 1,
  });

const token = document.cookie
        .split(';')
        .map(cookie => cookie.trim())
        .find(cookie => cookie.startsWith('token='))
        ?.split('=')[1];

  const sidebarItems = [
    { id: "adicionar", label: "Adicionar uma empresa" },
    { id: "listar", label: "Listar empresas cadastradas" },
    { id: "atualizar", label: "Atualizar dados de empresa" },
    { id: "apagar", label: "Apagar dados de empresa" },
  ]

  const API_URL = "http://localhost:8080/vagas";
  console.log("Cookies atuais: ", document.cookie);
  console.log("Este é o novo token: ", token);

useEffect(() => {
  if (!token) return;

  fetch(`${API_URL}/me`, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((data: Vaga[]) => {
      setVagas(data);
    })
    .catch((error) => {
      console.error("Erro ao buscar vagas da empresa: ", error);
    });
}, [token]);

  async function carregarVagaDaEmpresa() {
  try {
    const response = await fetch(`${API_URL}/me`, {
      method: "GET",
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
      },
    })

    if (!response.ok) {
      throw new Error("Erro ao buscar dados da vaga empresa logada")
    }

    const vaga = await response.json()
    setVaga(vaga)
  } catch (error) {
    console.error("Erro ao carregar vaga da empresa logada:", error)
  }
}

async function adicionarVaga(data: any) {
  const payload = {
    nome: data.nome,
    isActive: data.isActive,
    habilidades: data["habilidades"],
    idiomas: data["idiomas"],
    formacaoAcademica: data["formacaoAcademica"],
    experiencia: data["experiencia"],
    pesoHabilidades: data["pesoHabilidades"],
    pesoIdiomas: data["pesoIdiomas"],
    pesoFormacaoAcademica: data["pesoFormacaoAcademica"],
    pesoExperiencia: data["pesoExperiencia"],
  };

  try {
    const response = await fetch("http://localhost:8080/vagas", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao adicionar vaga.");
    }

    const result = await response.text();
    console.log("Vaga adicionada: ", result);
    alert("Vaga cadastrada com sucesso!");
  } catch (error: any) {
    console.error("Erro ao adicionar vaga: ", error);
    alert(`Erro ao adicionar vaga: ${error.message}`);
  }
}



  async function buscarVaga(data: any) {
    try {
      const response = await fetch(`${API_URL}/${data.busca}`, {
        method: 'GET',
        headers: {
          "Content-Type": "application/json",
          ...(token && { Authorization: `Bearer ${token}`}),
        },
      });

      if (!response.ok) { throw new Error(`Erro do servidor: ${response.status}`); }

      const result = await response.json();
      setVagaBuscada(result[0]);
      setVagas([]);
    } catch (error) {
        console.error("Erro ao buscar vaga: ", error); 
        alert(`Erro ao buscar vaga: ${error}`);
        setVagaBuscada(null);
      }
  }

  async function atualizarDadosVaga(data: any) {
    try {
      const response = await fetch(`${API_URL}/me`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          ...(token && {Authorization: `Bearer ${token}`}),
        },
        body: JSON.stringify(data),
      })

      if (!response.ok) { throw new Error(`Erro do servidor: ${response.status}`); }

      const result = await response.json()
      console.log("Vaga atualizada:", result)
      alert("Dados da vaga atualizados com sucesso!")
    } catch (error) {
      alert(`Erro ao atualizar vaga: ${error}`)
      console.error("Erro ao atualizar vaga:", error)
    }
  }


async function apagarVaga(data: any) {
  const confirmacao = window.confirm("Deseja mesmo excluir esta vaga? Esta ação é irreversível.");
  if (!confirmacao) return;

  try {
    const response = await fetch(`${API_URL}/${data.id}`, {
      method: "DELETE",
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
      },
    });

    if (response.ok) {
      console.log("Vaga removida com sucesso");
      alert("Vaga removida do sistema.");
      window.location.href = "/dashboard";
    } else {
      console.error("Erro ao remover vaga");
      alert("Erro ao remover vaga.");
    }
  } catch (error) {
    console.error("Erro ao apagar vaga:", error);
    alert("Erro inesperado ao tentar excluir a vaga.");
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
              { name: "nome", label: "Título da Vaga", type: "text" },
              { name: "habilidades", label: "Habilidades", type: "textarea" },
              { name: "idiomas", label: "Idiomas", type: "text" },
              { name: "formacaoAcademica", label: "Formação acadêmica", type: "text" },
              { name: "experiencia", label: "Tempo de experiência", type: "text" },
              { name: "pesoHabilidades", label: "Peso HABILIDADES", type: "number" },
              { name: "pesoIdiomas", label: "Peso IDIOMAS", type: "number" },
              { name: "pesoFormacaoAcademica", label: "Peso FORMAÇÃO ACADÊMICA", type: "number" },
              { name: "pesoExperiencia", label: "Peso EXPERIÊNCIA", type: "number" },
            ]}
            submitLabel="Adicionar Vaga"
            onSubmit={(data) => {
              const payload = {
                nome: data.nome,
                isActive: true,
                habilidades: data["habilidades"],
                idiomas: data["idiomas"],
                formacaoAcademica: data["formacaoAcademica"],
                experiencia: data["experiencia"],
                pesoHabilidades: Number(data["pesoHabilidades"]),
                pesoIdiomas: Number(data["pesoIdiomas"]),
                pesoFormacaoAcademica: Number(data["pesoFormacaoAcademica"]),
                pesoExperiencia: Number(data["pesoExperiencia"]),
              };

              adicionarVaga(payload);
            }}
          />



          <CrudSection
            id="listar"
            title="Listar vagas cadastradas"
            description="Visualize todas as vagas cadastradas na plataforma."
            fields={[{ name: "busca", label: "Buscar por título", type: "text" }]}
            submitLabel="Buscar"
            onSubmit={buscarVaga}
            showTable={true}
            tableHeaders={["Nome", "Ativa"]}
            tableData={
              vagaBuscada 
                ? [[ 
                  vagaBuscada.nome.toString() ?? "sem nome",
                  vagaBuscada.isActive.toString() ?? "ativa?"
                ]]
                : 
              vagas.map((vagas) => [
                  vagas.nome,
                  vagas.isActive.toString()

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
