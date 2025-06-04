"use client"

import { Bar, BarChart, CartesianGrid, XAxis, YAxis } from "recharts";
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
  ChartLegend,
  ChartLegendContent,
  type ChartConfig,
} from "@/components/ui/chart";
import { FileText, Users, UserSearch, Award, UserCheck } from "lucide-react"; 
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

const candidateFunnelData = [
  { stage: "applications", label: "Aplicações", count: 120, fill: "var(--color-applications)" },
  { stage: "screening", label: "Triagem IA", count: 95, fill: "var(--color-screening)" },
  { stage: "hr-interview", label: "Entrevista RH", count: 60, fill: "var(--color-hr-interview)" },
  { stage: "tech-interview", label: "Entrevista Técnica", count: 35, fill: "var(--color-tech-interview)" },
  { stage: "offer", label: "Oferta", count: 15, fill: "var(--color-offer)" },
  { stage: "hired", label: "Contratado", count: 10, fill: "var(--color-hired)" },
];

// configuração do gráfico (chart config)
const funnelChartConfig = {
  applications: {
    label: "Aplicações Recebidas",
    icon: FileText,
    color: "hsl(var(--chart-1))",
  },
  screening: {
    label: "Triagem (IA)",
    icon: UserSearch,
    color: "hsl(var(--chart-2))",
  },
  "hr-interview": {
    label: "Entrevista RH",
    icon: Users,
    color: "hsl(var(--chart-3))",
  },
  "tech-interview": {
    label: "Entrevista Técnica",
    icon: Users, 
    color: "hsl(var(--chart-4))", 
  },
  offer: {
    label: "Oferta Enviada",
    icon: Award,
    color: "hsl(var(--chart-5))",
  },
  hired: {
    label: "Contratado",
    icon: UserCheck,
    color: "hsl(var(--chart-6))",
  },
  count: {
    label: "Candidatos",
  }
} satisfies ChartConfig;

export function FunilCandidatosEmpresa() {
  return (
    <Card className="flex-1">
      <CardHeader>
        <CardTitle>Funil de Candidatos</CardTitle>
        <CardDescription>Número de candidatos por etapa do processo nos últimos 30 dias.</CardDescription>
      </CardHeader>
      <CardContent>
        <ChartContainer config={funnelChartConfig} className="min-h-[100px] w-full">
          <BarChart
            accessibilityLayer
            data={candidateFunnelData}
            margin={{
              top: 20,
              right: 20,
              bottom: 5,
              left: 20,
            }}
            layout="vertical" // leitura horizontal das labesl
          >
            <CartesianGrid horizontal={false} />
            <YAxis
              dataKey="label"
              type="category"
              tickLine={false}
              tickMargin={10}
              axisLine={false}
              width={120}
            />
            <XAxis dataKey="count" type="number" hide />
            <ChartTooltip
              cursor={{ fill: "hsl(var(--muted)/0.2"}}
              content={<ChartTooltipContent indicator="dot" hideLabel />}
            />
            <Bar
              dataKey="count"
              layout="vertical"
              radius={5} 
            >
            </Bar>
            <ChartLegend content={<ChartLegendContent nameKey="stage" verticalAlign="bottom" />}/>
          </BarChart>
        </ChartContainer>
      </CardContent>
    </Card>
  )
}


