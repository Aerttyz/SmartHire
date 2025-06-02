"use client"

import { Vaga } from "@/api/vaga.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { parseJwt } from "@/lib/utils"
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react"

export default function VagasPage() {
  const API_URL = "http://localhost:8080/vagas";
  const router = useRouter();
  const [token, setToken] = useState<string | null>(null);
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

  const sidebarItems = [
    { id: "adicionar", label: "Adicionar uma vaga" },
    { id: "listar", label: "Listar vaga cadastradas" },
    { id: "atualizar", label: "Atualizar dados de vaga" },
    { id: "apagar", label: "Apagar dados de vaga" },
  ]

    useEffect(() => {
        if (typeof document !== 'undefined') { 
            const cookieToken = document.cookie
                .split(';')
                .map(cookie => cookie.trim())
                .find(cookie => cookie.startsWith('token='))
                ?.split('=')[1];
            setToken(cookieToken || null);
            console.log("Cookies atuais (no useEffect): ", document.cookie);
            console.log("Este é o token extraído (no useEffect): ", cookieToken);
        }
    }, []);

      useEffect(() => {
        if (!token) {
            console.log("Token não disponível ainda. Aguardando...");
            return;
        }

        fetch(`${API_URL}/me`, {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        })
        .then((res) => {
            if (!res.ok) {
                return res.json().then(err => { throw new Error(err.message || res.statusText); });
            }
            return res.json();
        })
        .then((data: Vaga[]) => {
            setVagas(data);
        })
        .catch((error) => {
            console.error("Erro ao buscar vagas da empresa: ", error);
        });
    }, [token]); // só roda quando token for definido


   const handleEditVagaClick = (vagaId: string) => {
    console.log("Editando vaga com ID:", vagaId);
    router.push(`/vagas/me/editar/${vagaId}`);
  };

  const handleDeleteVagaClick = async (vagaId: string) => {
    const confirmacao = window.confirm(`Deseja mesmo excluir a vaga com ID: ${vagaId}? Esta ação é irreversível.`);
    if (!confirmacao) return;

    try {
      const response = await fetch(`${API_URL}/${vagaId}`, { 
        method: "DELETE",
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      });

      if (response.ok) {
        console.log(`Vaga ${vagaId} removida com sucesso`);
        alert("Vaga removida do sistema.");
        // Atualizar a lista de vagas após a exclusão
        setVagas(prevVagas => prevVagas.filter(vaga => vaga.id !== vagaId));
        setVagaBuscada(null); // Limpar vaga buscada se for o caso
      } else {
        const errorData = await response.json();
        console.error("Erro ao remover vaga: ", errorData);
        alert(`Erro ao remover vaga: ${errorData.message || response.statusText}`);
      }
    } catch (error) {
      console.error("Erro ao apagar vaga:", error);
      alert("Erro inesperado ao tentar excluir a vaga.");
    }
  };

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
    const vagaId = data.id;
    const decodedToken = token ? parseJwt(token) : null;

    if (!decodedToken || !decodedToken.empresaId) {
      throw new Error("Token inválido ou empresaId não encontrado.");
    }

    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    if (!uuidRegex.test(vagaId)) {
      throw new Error("O ID fornecido não é um UUID válido.");
    }

    const { id, ...payload } = data; // Desestrutura para pegar todos os dados, menos o id

    payload.pesoHabilidades = Number(payload.pesoHabilidades);
    payload.pesoIdiomas = Number(payload.pesoIdiomas);
    payload.pesoFormacaoAcademica = Number(payload.pesoFormacaoAcademica);
    payload.pesoExperiencia = Number(payload.pesoExperiencia);
    payload.empresaId = decodedToken.empresaId;

    const response = await fetch(`${API_URL}/${vagaId}`, { 
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
      },
      body: JSON.stringify(payload), 
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || `Erro do servidor: ${response.status}`);
    }

    const result = await response.json();
    console.log("Vaga atualizada:", result);
    alert("Dados da vaga atualizados com sucesso!");
  } catch (error: any) {
    alert(`Erro ao atualizar vaga: ${error.message || error}`);
    console.error("Erro ao atualizar vaga:", error);
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
            tableHeaders={["Nome", "ID", "Ativa"]}
            tableData={
              vagaBuscada 
                ? [[ 
                  vagaBuscada.nome.toString() ?? "sem nome",
                  vagaBuscada.id?.toString() ?? "sem id",
                  vagaBuscada.isActive.toString() ?? "ativa?"
                ]]
                : 
              vagas.map((vagas) => [
                  vagas.nome,
                  vagas.id!.toString(),
                  vagas.isActive.toString()

              ])
            }
            onDeleteClick={handleDeleteVagaClick}
            onEditClick={handleEditVagaClick}
          />
          
          <CrudSection
            id="atualizar"
            title="Atualizar dados de vaga"
            description="Selecione uma vaga e atualize seus dados."
            fields={[
              { name: "id", label: "ID da Vaga", type: "text" },
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

            submitLabel="Atualizar Vaga"
            onSubmit={atualizarDadosVaga}
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
