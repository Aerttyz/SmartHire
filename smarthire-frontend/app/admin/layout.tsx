import type React from "react"
import { MainNav } from "@/components/dashboard/main-nav"
import { UserNav } from "@/components/dashboard/user-nav"
import { ModeToggle } from "@/components/mode-toggle"

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="flex min-h-screen flex-col">
      <header className="sticky top-0 z-10 border-b bg-background">
        <div className="container flex h-16 items-center justify-between px-4">
          <MainNav />
          <div className="flex items-center gap-4">
            <ModeToggle />
            <UserNav />
          </div>
        </div>
      </header>
      <div className="container flex-1 px-4 py-6 md:py-8">{children}</div>
      <footer className="border-t py-4">
        <div className="container px-4 text-center text-sm text-muted-foreground">
          &copy; {new Date().getFullYear()} SmartHire. Todos os direitos reservados.
        </div>
      </footer>
    </div>
  )
}
