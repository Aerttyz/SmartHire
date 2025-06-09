"use client"

import { Vaga } from "@/api/vaga.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { useRouter } from "next/navigation"; 
import { use, useEffect, useState } from "react"

export default function AtualizarVagaPage({ params }: { params: Promise<{ vagaId: string }> }) {

  const { vagaId } = use(params);
  const router = useRouter();

  const [vaga, setVaga] = useState<Vaga | null>(null); 

  const token = document.cookie
    .split(';')
    .map(cookie => cookie.trim())
    .find(cookie => cookie.startsWith('token='))
    ?.split('=')[1];

  const sidebarItems = [
    { id: "atualizar", label: "Atualizar dados de vaga" },
  ]

  const API_URL = "http://localhost:8080/vagas";
  console.log("Cookies atuais: ", document.cookie);
  console.log("Este é o novo token: ", token);

  useEffect(() => {
    if (!token || !vagaId) return;

    const fetchVagaData = async () => {
      try {
        const response = await fetch(`${API_URL}/id/${vagaId}`, { 
          method: 'GET',
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Erro ao buscar dados da vaga: ${response.statusText}`);
        }

        const data: Vaga = await response.json();
        setVaga(data);
      } catch (error) {
        console.error("Erro ao carregar dados da vaga para edição: ", error);
        alert("Não foi possível carregar os dados da vaga para edição.");
        router.push('/vagas');
      }
    };

    fetchVagaData();
  }, [vagaId, token, router]); 

  async function atualizarDadosVaga(data: any) {
    try {
      
      const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
      if (!uuidRegex.test(vagaId)) {
        throw new Error("O ID da URL não é um UUID válido.");
      }
      const { id, ...payload } = data;

      payload.pesoHabilidades = Number(payload.pesoHabilidades);
      payload.pesoIdiomas = Number(payload.pesoIdiomas);
      payload.pesoFormacaoAcademica = Number(payload.pesoFormacaoAcademica);
      payload.pesoExperiencia = Number(payload.pesoExperiencia);

      if (payload.isActive !== undefined) {
         payload.isActive = String(payload.isActive).toLowerCase() === 'true' || String(payload.isActive).toLowerCase() === 'on';
      } else {
        payload.isActive = false; 
      }

      const response = await fetch(`${API_URL}/${vagaId}`, { // Usa o vagaId dos params na URL
        method: "PATCH",
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
      router.push('/vagas'); 

    } catch (error: any) {
      alert(`Erro ao atualizar vaga: ${error.message || error}`);
      console.error("Erro ao atualizar vaga:", error);
    }
  }

  if (!vaga) {
    return (
      <DashboardShell>
        <DashboardHeader heading="Carregando Vaga..." text="Buscando os dados da vaga para edição." />
        <div className="flex flex-col gap-8 md:flex-row">
          <DashboardSidebar items={sidebarItems} />
          <div className="flex-1 space-y-8">
            <p>Carregando...</p>
          </div>
        </div>
      </DashboardShell>
    );
  }

  return (
    <DashboardShell>
      <DashboardHeader heading={`Atualizar Vaga: ${vaga.nome}`} text="Edite os detalhes desta vaga." />
      <div className="flex flex-col gap-8 md:flex-row">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <CrudSection
            id="atualizar"
            title={`Atualizar Vaga: ${vaga.nome}`}
            description={`Editando a vaga com ID: ${vagaId}`}
            fields={[
              { name: "nome", label: "Título da Vaga", type: "text", defaultValue: vaga.nome },
              { name: "habilidades", label: "Habilidades", type: "textarea", defaultValue: vaga.requisitos?.habilidades },
              { name: "idiomas", label: "Idiomas", type: "text", defaultValue: vaga.requisitos?.idiomas },
              { name: "formacaoAcademica", label: "Formação acadêmica", type: "text", defaultValue: vaga.requisitos?.formacaoAcademica },
              { name: "experiencia", label: "Tempo de experiência", type: "text", defaultValue: vaga.requisitos?.experiencia },
              { name: "pesoHabilidades", label: "Peso HABILIDADES", type: "number", defaultValue: vaga.requisitos?.pesoHabilidades?.toString() },
              { name: "pesoIdiomas", label: "Peso IDIOMAS", type: "number", defaultValue: vaga.requisitos?.pesoIdiomas?.toString() },
              { name: "pesoFormacaoAcademica", label: "Peso FORMAÇÃO ACADÊMICA", type: "number", defaultValue: vaga.requisitos?.pesoFormacaoAcademica?.toString() },
              { name: "pesoExperiencia", label: "Peso EXPERIÊNCIA", type: "number", defaultValue: vaga.requisitos?.pesoExperiencia?.toString() },
              { name: "isActive", label: "Ativa", type: "checkbox", defaultValue: vaga.isActive },
            ]}
            submitLabel="Salvar Alterações"
            onSubmit={atualizarDadosVaga}
          />
        </div>
      </div>
    </DashboardShell>
  )
}