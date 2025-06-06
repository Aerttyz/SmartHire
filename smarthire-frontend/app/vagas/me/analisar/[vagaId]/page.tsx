"use client"

import { AvaliacaoLLM } from "@/api/avaliacao.api"
import { Candidato } from "@/api/candidato.api"
import { Vaga } from "@/api/vaga.api"
import { CrudTable } from "@/components/crud/crud-table"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { Card, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { useRouter } from "next/navigation"; 
import { use, useEffect, useState } from "react"

export default function AnalisarVagaPage({ params }: { params: Promise<{ vagaId: string }> }) {

  const { vagaId } = use(params);
  const router = useRouter();
  const [vaga, setVaga] = useState<Vaga | null>(null); 
  const [candidatoId, setCandidatoId] = useState<string>("");
  const [candidatos, setCandidatos] = useState<Candidato[]>([]);
  const [token, setToken] = useState<string | null>(null); 
  const [avaliacaoCurriculo, setAvaliacaoCurriculo] = useState<AvaliacaoLLM | null>(null);
  const [toggleAvaliacao, setToggleAvaliacao] = useState<boolean>(false);

  useEffect(() => {
    if (typeof document !== 'undefined') {
      const cookieToken = document.cookie
          .split(';')
          .map(cookie => cookie.trim())
          .find(cookie => cookie.startsWith('token='))
          ?.split('=')[1];
      setToken(cookieToken || null);
        }
    }, []);

  const sidebarItems = [
    { id: "curriculos", label: "Currículos da empresa" },
    { id: "analisar", label: "Análise de Compatibilidade" },
  ]

  const API_URL = "http://localhost:8080";

  const avaliarCompatibilidadeCandidatoVaga = async (idCandidato: string) => {
    if (!idCandidato) {
      alert("Erro: Id do candidato não fornecido para avaliação.");
      return;
    }
    try {
      const response = await fetch(`${API_URL}/curriculos/avaliar/${vagaId}/${idCandidato}`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Erro ao avaliar currículo: ${response.statusText}`);
      }
      
      const data: AvaliacaoLLM = await response.json();
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
      listarCandidatosEmpresa();
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
      return;
    }
    fetchVagaData();
  }, [vagaId, token]);

  const handleAnalyzeCandidatoClick = async (idDoCandidatoClicado: string) => {
    setToggleAvaliacao(false);
    setAvaliacaoCurriculo(null);
    await avaliarCompatibilidadeCandidatoVaga(idDoCandidatoClicado);
    setToggleAvaliacao(true);
    setCandidatoId(idDoCandidatoClicado);
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
      <DashboardHeader heading={`Análise de Compatibilidade Inteligente para vaga: ${vaga.nome}`} text="Selecione um candidato para iniciar a avaliação por IA." />
      <div className="flex flex-col gap-8 md:flex-row">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <Card>
            <CardHeader>
              <CardTitle>Listar candidatos</CardTitle>
              <CardDescription>Exibição de todos os candidatos vinculados à empresa</CardDescription>
            </CardHeader>
            <CrudTable
              headers={["Nome", "E-mail", "Telefone", "Id do candidato"]}
              data={ candidatos.map((c: Candidato) => [
                  c.nome,
                  c.email,
                  c.telefone,
                  c.id,
                ])}
                idColumnIndex={3}
              onAnalyzeClick={handleAnalyzeCandidatoClick}
            />
          </Card>

          {/* Passo 2: Usar o componente de exibição atualizado */}
          {toggleAvaliacao && avaliacaoCurriculo && (
            <Card>
              <CardHeader>
                <CardTitle>Resultado da Análise de Compatibilidade</CardTitle>
                <CardDescription>
                  Análise gerada por IA para o candidato selecionado.
                </CardDescription>
              </CardHeader>
              <div className="p-6 pt-0 text-sm">
                <div className="grid gap-4">
                  <div>
                    <h4 className="font-semibold mb-1">Nível de Compatibilidade</h4>
                    <p className="text-muted-foreground">{avaliacaoCurriculo.compatibilidade}</p>
                  </div>
                  <hr/>
                  <div>
                    <h4 className="font-semibold mb-1">Pontos Fortes</h4>
                    <p className="text-muted-foreground whitespace-pre-wrap">{avaliacaoCurriculo.pontosFortes}</p>
                  </div>
                  <hr/>
                  <div>
                    <h4 className="font-semibold mb-1">Lacunas Identificadas (Pontos a Melhorar)</h4>
                    <p className="text-muted-foreground whitespace-pre-wrap">{avaliacaoCurriculo.lacunasIdentificadas}</p>
                  </div>
                  <hr/>
                  <div>
                    <h4 className="font-semibold mb-1">Sugestões para a Empresa</h4>
                    <p className="text-muted-foreground whitespace-pre-wrap">{avaliacaoCurriculo.sugestoesParaEmpresa}</p>
                  </div>
                </div>
              </div>
            </Card>
          )}

        </div>
      </div>
    </DashboardShell>
  )
}