"use client";

import React, { useEffect, useState } from "react";
import { Pie, PieChart, Cell } from "recharts";
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
  ChartLegend,
  ChartLegendContent,
  type ChartConfig,
} from "@/components/ui/chart";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Target, Users } from "lucide-react";

const chartConfig = {
  empresaLogada: {
    label: "Sua Empresa: ",
    icon: Target,
    color: "hsl(var(--chart-1))",
  },
  demaisEmpresas: {
    label: "Demais Empresas Credenciadas: ",
    icon: Users,
    color: "hsl(var(--chart-2))",
  },
} satisfies ChartConfig;

export function MarketSharePieChart() {
  const [numeroCandidatosTotal, setNumeroCandidatosTotal] = useState<number>(0);
  const [numeroCandidatosEmpresa, setNumeroCandidatosEmpresa] = useState<number>(0);
  const [token, setToken] = useState<string | null>(null);

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

  useEffect(() => {
    if (!token) return;

    const buscarDados = async () => {
      try {
        const totalRes = await fetch("http://localhost:8080/candidatos", {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        const totalData = await totalRes.json();
        setNumeroCandidatosTotal(totalData.length);
        console.log("numero total de candidatos: ", numeroCandidatosTotal);

        const empresaRes = await fetch("http://localhost:8080/candidatos/me", {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        const empresaData = await empresaRes.json();
        setNumeroCandidatosEmpresa(empresaData);
        console.log("numero de candidatos da empresa: ", numeroCandidatosEmpresa);
      } catch (error) {
        console.error("Erro ao buscar dados dos candidatos:", error);
      }
    };

    buscarDados();
  }, [token]);

  const pieData = [
    {
      sliceId: "empresaLogada",
      label: chartConfig.empresaLogada.label,
      value: numeroCandidatosEmpresa,
      fill: chartConfig.empresaLogada.color,
    },
    {
      sliceId: "demaisEmpresas",
      label: chartConfig.demaisEmpresas.label,
      value: (numeroCandidatosTotal - numeroCandidatosEmpresa),
      fill: chartConfig.demaisEmpresas.color,
    },
  ];

  const total = numeroCandidatosTotal;
  const percentage = total > 0 ? Math.round((numeroCandidatosEmpresa / total) * 100) : 0;

  return (
    <Card className="flex flex-1 flex-col">
      <CardHeader>
        <CardTitle>Participação de Mercado</CardTitle>
        <CardDescription>
          Comparativo da sua empresa com as demais cadastradas. Total de {total} currículos considerados.
        </CardDescription>
      </CardHeader>
      <CardContent className="flex flex-1 flex-col items-center justify-center pb-4">
        <ChartContainer
          config={chartConfig}
          className="relative mx-auto aspect-square h-[250px] w-full max-w-[250px]"
        >
          <PieChart>
            <ChartTooltip cursor={false} content={<ChartTooltipContent hideLabel />} />
            <Pie
              data={pieData}
              dataKey="value"
              nameKey="label"
              cx="50%"
              cy="50%"
              innerRadius={65}
              outerRadius={90}
              paddingAngle={1}
              strokeWidth={1}
            >
              {pieData.map((entry) => (
                <Cell
                  key={entry.sliceId}
                  fill={entry.fill}
                  className="focus:outline-none focus-visible:ring-1 focus-visible:ring-ring focus-visible:ring-offset-1"
                />
              ))}
            </Pie>
            {total > 0 && (
              <foreignObject x="50%" y="50%" width="1" height="1" style={{ overflow: 'visible' }}>
                <div
                  style={{
                    position: 'absolute',
                    left: '50%',
                    top: '50%',
                    transform: 'translate(-50%, -50%)',
                    textAlign: 'center',
                    pointerEvents: 'none',
                  }}
                >
                  <span className="text-3xl font-bold text-foreground">
                    {percentage}%
                  </span>
                  <span className="mt-1 block text-xs text-muted-foreground">
                    Share
                  </span>
                </div>
              </foreignObject>
            )}
            <div className="mt-auto pt-4 flex w-full justify-center">
              <ChartLegend
                content={<ChartLegendContent nameKey="label" className="flex-row" />}
                className="[&_.recharts-legend-item]:w-max"
              />
            </div>
          </PieChart>
        </ChartContainer>
      </CardContent>
    </Card>
  );
}
