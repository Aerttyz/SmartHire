import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardShell } from "@/components/dashboard/dashboard-shell"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"

export default function DashboardPage() {
  return (
    <DashboardShell>
      <DashboardHeader heading="Bem-vindo ao SmartHire" text="Gerencie vagas, currículos e empresas em um só lugar." />
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
