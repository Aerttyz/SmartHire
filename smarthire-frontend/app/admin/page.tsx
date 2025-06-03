"use client"

import { Empresa } from "@/api/empresa.api"
import { CrudSection } from "@/components/crud/crud-section"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"
import { useEffect, useState } from "react"

export default function AdminPage() {
  const API_URL = "http://localhost:8080/empresas";
  const [empresas, setEmpresas] = useState<Empresa[]>([]);
  const [empresa, setEmpresa ] = useState<Empresa>({nome: "", cnpj: "", telefone: "", email: "", senha: "" });
  const [empresaBuscada, setEmpresaBuscada] = useState<Empresa | null>(null);
  const [token, setToken] = useState<string | null>(null);

  const sidebarItems = [
    { id: "atualizar", label: "Atualizar dados de empresa" },
    { id: "apagar", label: "Apagar dados de empresa" },
  ]

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

    useEffect(() => {
        if (!token) {
            console.log("Token não disponível ainda para AdminPage. Aguarde");
            return;
        }

        fetch(API_URL, { 
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        })
            .then(async (res) => {
                if (!res.ok) {
                    return res.json().then(err => { throw new Error(err.message || res.statusText); });
                }
                return res.json();
            })
            .then((data: Empresa[]) => {
                setEmpresas(data);
            })
            .catch((error) => {
                console.error("Erro ao buscar empresas: ", error);
            });
    }, [token]);
  

  async function atualizarEmpresa(data: any) {
    try {
      const response = await fetch(`${API_URL}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          ...(token && {Authorization: `Bearer ${token}`}),
        },
        body: JSON.stringify(data),
      })

      if (!response.ok) { throw new Error(`Erro do servidor: ${response.status}`); }

      const result = await response.json()
      console.log("Empresa atualizada:", result)
      alert("Dados da empresa atualizados com sucesso!")
    } catch (error) {
      alert(`Erro ao atualizar empresa: ${error}`)
      console.error("Erro ao atualizar empresa:", error)
    }
  }


async function apagarEmpresa() {
  const confirmacao = window.confirm("Deseja mesmo excluir sua empresa? Esta ação é irreversível.");

  if (!confirmacao) {
    return;
  }

  try {
    const token = document.cookie
      .split("; ")
      .find((row) => row.startsWith("token="))
      ?.split("=")[1];

    const response = await fetch("http://localhost:8080/empresas", {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      console.log("Empresa removida com sucesso");
      alert("Empresa removida do sistema.");
      
      window.location.href = "/auth/login";
    } else {
      console.error("Erro ao remover empresa");
      alert("Erro ao remover empresa.");
    }
  } catch (error) {
    console.error("Erro ao apagar empresa:", error);
    alert("Erro inesperado ao tentar excluir a empresa.");
  }
}


  return (
    <DashboardShell>
      <DashboardHeader heading="Área do Administrador" text="Gerencie as empresas cadastradas na plataforma." />
      <div className="flex flex-col gap-8 md:flex-row py-10">
        <DashboardSidebar items={sidebarItems} />
        <div className="flex-1 space-y-8">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Vagas</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">Oportunidades</div>
                <p className="text-xs text-muted-foreground">Crie e gerencie vagas de emprego</p>
                <div className="mt-4">
                  <Link href="/vagas">
                    <Button>Acessar</Button>
                  </Link>
                </div>
              </CardContent>
            </Card>
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Currículos</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">Candidatos</div>
                <p className="text-xs text-muted-foreground">Analise currículos com inteligência artificial</p>
                <div className="mt-4">
                  <Link href="/curriculos">
                    <Button>Acessar</Button>
                  </Link>
                </div>
              </CardContent>
            </Card>
          </div>
  
          <CrudSection
            id="atualizar"
            title="Atualizar dados cadastrais"
            description="Selecione uma empresa e atualize seus dados."
            fields={[
              { name: "nome", label: "Nome da Empresa", type: "text", required: false },
              { name: "cnpj", label: "CNPJ", type: "text", required: false },
              { name: "telefone", label: "Telefone", type: "text", required: false },
              { name: "email", label: "Email", type: "email", required: false },
              { name: "senha", label: "Senha", type: "password", required: false },
            ]}
            submitLabel="Atualizar Empresa"
            onSubmit={atualizarEmpresa}
          />

          
          <CrudSection
            id="apagar"
            title="Apagar dados de empresa"
            description="Selecione uma empresa para remover do sistema."
            fields={[]}
            submitLabel="Apagar Empresa"
            onSubmit={apagarEmpresa}
            isDanger={true}
          />
        </div>
      </div>
    </DashboardShell>
  )
}
