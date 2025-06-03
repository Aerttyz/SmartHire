"use client"

import { Candidato } from "@/api/candidato.api"
import { Vaga } from "@/api/vaga.api"
import { CrudSection } from "@/components/crud/crud-section"
import { CrudTable } from "@/components/crud/crud-table"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { Card, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { parseJwt } from "@/lib/utils"
import { useRouter } from "next/navigation"; 
import { useEffect, useState } from "react"
import { ReadOnlyTextDisplay } from "@/components/ui/read-only-text-display";
import {
  FormItem,
  FormLabel,
  FormControl,
  FormDescription,
} from "@/components/ui/form";

export default function AtualizarVagaPage({ params }: { params: { vagaId: string } }) {

  const { vagaId } = params;
  const router = useRouter();
  const [vaga, setVaga] = useState<Vaga | null>(null); 
  const [curriculoId, setCurriculoId] = useState<string | null>(null);
  const [candidatos, setCandidatos] = useState<Candidato[]>([]);
  const [token, setToken] = useState<string | null>(null); 
  const [avaliacaoCurriculo, setAvaliacaoCurriculo] = useState<string | null>(null);
  const [toggleAvaliacao, setToggleAvaliacao] = useState<boolean>(false);

  useEffect(() => {
    if (typeof document !== 'undefined') {
      const cookieToken = document.cookie
          .split(';')
          .map(cookie => cookie.trim())
          .find(cookie => cookie.startsWith('token='))
          ?.split('=')[1];
      setToken(cookieToken || null);
      console.log("Cookies atuais: ", document.cookie);
      console.log("Este é o token extraído: ", cookieToken);
        }
    }, []);

  const sidebarItems = [
    { id: "curriculos", label: "Currículos da empresa" },
    { id: "analisar", label: "Análise de Compatibilidade" },
  ]

  const API_URL = "http://localhost:8080";
  console.log("Cookies atuais: ", document.cookie);
  console.log("Este é o novo token: ", token);

  const avaliarCompatibilidadeCandidatoVaga = async () => {
    try {
      const response = await fetch(`${API_URL}/curriculos/avaliar/${vagaId}/${curriculoId}`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Erro ao avaliar currículo: ${response.statusText}`);
      }
      const data: string = await response.json();
      setAvaliacaoCurriculo(data);
    } catch (error) {
      console.error("Erro ao realizar análise para o currículo: ", error);
      alert("Erro ao realizar análise para o currículo.");
      router.push('/vagas');
    }
  };

  const listarCandidatosEmpresa = async () => {
    try {
      const response = await fetch(`${API_URL}/candidatos/me/listar`, { 
        method: 'GET',
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Erro ao buscar dados de candidatos: ${response.statusText}`);
      }
      const data: Candidato[] = await response.json();
      setCandidatos(data);
    } catch (error) {
      console.error("Erro ao carregar dados de candidatos para análise: ", error);
      alert("Não foi possível carregar os dados do candidato.");
      router.push('/vagas');
    }
  };

useEffect(() => {
  if (token) { 
    console.log("Chamando listarCandidatosEmpresa com token:", token);
    listarCandidatosEmpresa();
  } else {
    console.log("Token ainda não disponível para listarCandidatosEmpresa.");
  }
}, [token]);

const fetchVagaData = async () => {
  try {
    const response = await fetch(`${API_URL}/vagas/id/${vagaId}`, { 
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

useEffect(() => {
  if (!token || !vagaId) {
    console.log("Token ou vagaId faltando para fetchVagaData.");
    return;
  }
  fetchVagaData();
}, [vagaId, token]);

  const handleAnalyzeCandidatoClick = async (candidatoId: string) => {
    setToggleAvaliacao(!toggleAvaliacao);
    setCurriculoId(candidatoId);
    avaliarCompatibilidadeCandidatoVaga();
  }

  const backendTextValue = "Este é um texto longo do backend que deve quebrar a linha e expandir o campo conforme necessário para exibir todo o conteúdo."

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
      <DashboardHeader heading={`Análise de Compatibilidade Inteligente para vaga: ${vaga.nome}`} text="Informe o currículo desejado a seguir para dar início à avaliação." />
      <div className="flex flex-col gap-8 md:flex-row">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <Card>
            <CardHeader>
              <CardTitle>Listar candidatos</CardTitle>
              <CardDescription>Exibição de todos os candidatos vinculados à empresa</CardDescription>
            </CardHeader>
            <CrudTable
            
            headers={["Nome", "E-mail", "Telefone", "Currículo", "Ações"]}
            data={
              candidatos.map((candidatos: Candidato) => [
                  candidatos.nome,
                  candidatos.email,
                  candidatos.telefone,
                  candidatos.curriculoId,
              ])
            }
            onAnalyzeClick={handleAnalyzeCandidatoClick}

          />
          </Card>

          {toggleAvaliacao && <ReadOnlyTextDisplay value={avaliacaoCurriculo} placeholder="Nenhuma descrição disponível." />}

        </div>
      </div>
    </DashboardShell>
  )
}