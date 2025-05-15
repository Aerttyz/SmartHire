"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { Button } from "@/components/ui/button"
import Image from "next/image"

export function MainNav() {
  const pathname = usePathname()

  return (
    <div className="flex items-center gap-6">
      <Link href="/dashboard" className="flex items-center gap-2">
        <Image
          src="/placeholder.svg?height=32&width=32"
          alt="SmartHire Logo"
          width={32}
          height={32}
          className="h-8 w-8"
        />
        <span className="hidden font-bold sm:inline-block">SmartHire</span>
      </Link>
      <nav className="flex items-center gap-4">
        <Link href="/admin">
          <Button variant={pathname.startsWith("/admin") ? "default" : "ghost"} className="h-8 px-3">
            Área do Administrador
          </Button>
        </Link>
        <Link href="/vagas">
          <Button variant={pathname.startsWith("/vagas") ? "default" : "ghost"} className="h-8 px-3">
            Vagas
          </Button>
        </Link>
        <Link href="/curriculos">
          <Button variant={pathname.startsWith("/curriculos") ? "default" : "ghost"} className="h-8 px-3">
            Análise de Currículos
          </Button>
        </Link>
      </nav>
    </div>
  )
}
