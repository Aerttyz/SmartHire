"use client"

import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ChartContainer } from "@/components/ui/chart"
import Link from "next/link"
import { FunilCandidatosEmpresa } from "@/components/dashboard/charts/candidato-funnel-chart"
import { MarketSharePieChart } from "@/components/dashboard/charts/market-share-candidatos-pie-chart"
import { useEffect, useState } from "react"
import { Vaga } from "@/api/vaga.api"

export default function DashboardPage() {
    const API_URL = "http://localhost:8080/vagas";
    const [token, setToken] = useState<string | null>(null);
    const [vagas, setVagas] = useState<Vaga[]>([]);
    const [media, setMedia] = useState<number>(0);
    const [numeroEmpresas, setNumeroEmpresas] = useState<number>(0);
    const [numeroVagas, setNumeroVagas] = useState<number>(0);

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

    const contarEmpresas = async (token: string, setNumeroEmpresas: (n: number) => void) => {
      try {
          const res = await fetch(`http://localhost:8080/empresas`, { 
            method: 'GET',
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
             },
          })
          .then(async (res) => {
            if(!res.ok) {
              const err = await res.json();
              throw new Error(err.message || res.statusText);
            }
            return res.json();
          })
          .then((data) => {
            setNumeroEmpresas(data.length),
            console.log("Número de empresas: ", numeroEmpresas);
          })
        } catch (error) {
          console.error("Erro ao buscar empresas:", error);
        }
      }


      useEffect(() => {
        if (!token) {
          console.log("Token não disponível ainda para AdminPage. Aguarde");
          return;
         }
        contarEmpresas(token, setNumeroEmpresas);
      }, [token]);

    useEffect(() => {
      if (!token) {
        console.log("Token não disponível ainda. Aguardando...");
        return;
      }

      fetch(`http://localhost:8080/candidatos/me/media`, {
        method: 'GET',
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        }
      })
      .then(async (res) => {
        if (!res.ok) {
          const err = await res.json()
          throw new Error(err.message || res.statusText)
        }
        return res.json();
      })
      .then((data: number) => {
        setMedia(data);
        console.log("Essa é a média: ", media);
      })
    }, [token])

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
          .then(async (res) => {
              if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || res.statusText);
              }
              return res.json();
          })
          .then((data: Vaga[]) => {
              setVagas(data);
              setNumeroVagas(data.length);
              console.log(data)
              console.log("número de vagas: ", data.length)
          })
          .catch((error) => {
              console.error("Erro ao buscar vagas da empresa: ", error);
          });
      }, [token]); 

  return (
    <DashboardShell>
      <DashboardHeader heading="Painel de Controle" text="Bem-vinda, Empresa. Visualize estatísticas, gerencie vagas, currículos e empresas em um só lugar." />
      <section className="mt-4 flex flex-col md:flex-row gap-4 lg:grid-cols-3">
        <MarketSharePieChart />
        <div className="flex-column">
          <Card className="mb-4">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Empresas credenciadas no SmartHire</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl">{numeroEmpresas}</div>
            </CardContent>
          </Card>
          <Card className="mb-4">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Vagas abertas pela empresa</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl">{numeroVagas}</div>
            </CardContent>
          </Card>
          <Card className="mb-4">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Média de candidatos por vaga</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl">{media}</div>
            </CardContent>
          </Card>
        </div>
      </section>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Área do Administrador</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">Empresas</div>
            <p className="text-xs text-muted-foreground">Gerencie empresas cadastradas na plataforma</p>
            <div className="mt-4">
              <Link href="/admin">
                <Button>Acessar</Button>
              </Link>
            </div>
          </CardContent>
        </Card>
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
    </DashboardShell>
  )
}
