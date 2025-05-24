"use client"

import { CrudSection } from "@/components/crud/crud-section"
import { Card, CardFooter} from "@/components/ui/card"
import Link from "next/link"

export default function LoginPage() {
  
  const API_URL = "http://localhost:8080/auth/cadastrar";

  async function adicionarEmpresa(data: any) {
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
      const result = await response.json()
      console.log("Empresa adicionada:", result)
      alert("Empresa adicionada com sucesso!");
      window.location.href = "/auth/login";
    } catch (error) {
      console.error("Erro ao adicionar empresa:", error)
      alert(`Erro ao adicionar empresa: ${error}`);
    }
  }
    
  return (
    <div className="flex min-h-screen items-center justify-center bg-muted/40 px-4">
      <Card className="w-full max-w-md">
        <CrudSection
                    id="adicionar"
                    title="Cadastro"
                    description="Preencha as informações sobre sua empresa para se cadastrar."
                    fields={[
                      { name: "nome", label: "Nome da Empresa", type: "text" },
                      { name: "cnpj", label: "CNPJ", type: "text" },
                      { name: "telefone", label: "Telefone", type: "text" },
                      { name: "email", label: "Email", type: "email" },
                      { name: "senha", label: "Senha", type: "password" },
                    ]}
                    submitLabel="Adicionar Empresa"
                    onSubmit={adicionarEmpresa}
                  />

        <CardFooter className="flex flex-col space-y-4">
          <br/>
          <div className="text-center text-sm">
              Já possui conta? <Link href="/login" className="text-muted-foreground hover:underline">Cadastre-se aqui</Link>
          </div>
        </CardFooter>
      </Card>
    </div>
  )
}
