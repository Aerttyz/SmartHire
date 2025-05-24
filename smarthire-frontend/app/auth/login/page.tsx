"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import Link from "next/link"
import { useState } from "react"

export default function LoginPage() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")  
    
  const API_URL = "http://localhost:8080/auth/login";

  async function logarEmpresa(data: any) {
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({email, senha: password}),
      })
      if (!response.ok) {
        const error = await response.text();
        throw new Error(error || "Erro ao autenticar");
      }

      const result = await response.json()
      const token = result.token;
      document.cookie = 
        `token=${token}; 
          path=/; 
          max-age=9000; 
          secure
        `
      console.log("Empresa logada:", result)
      alert("Empresa logada com sucesso!");
      window.location.href = "/dashboard";

    } catch (error) {
      console.error("Erro ao logar empresa:", error)
      alert(`Erro ao logar empresa: ${error}`);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-muted/40 px-4">
      <Card className="w-full max-w-md">
        <form onSubmit={logarEmpresa}>
          <CardHeader className="space-y-1 text-center">
            <CardTitle className="text-2xl font-bold">Entrar no SmartHire</CardTitle>
            <CardDescription>Entre com suas credenciais para acessar a plataforma</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input 
                id="email" 
                type="email" 
                placeholder="seu@email.com" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Senha</Label>
              <Input 
                id="password" 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </CardContent>
          <CardFooter className="flex flex-col space-y-4">
            <Link href="/dashboard" className="w-full">
              <Button className="w-full" type="submit">Entrar</Button>
            </Link>
            <div className="text-center text-sm">
              <Link href="#" className="text-muted-foreground hover:underline">
                Esqueceu sua senha?
              </Link>
            </div>
            <div className="text-center text-sm">
                Ainda não possui conta? <Link href="/auth/cadastrar" className="text-muted-foreground hover:underline">Cadastre-se aqui</Link>
            </div>
          </CardFooter>
        </form>
      </Card>
    </div>
  )
}
