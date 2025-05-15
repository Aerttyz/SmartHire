"use client"

import { cn } from "@/lib/utils"
import { useScrollTo } from "@/hooks/use-scroll-to"

interface SidebarItem {
  id: string
  label: string
}

interface DashboardSidebarProps {
  items: SidebarItem[]
  className?: string
}

export function DashboardSidebar({ items, className }: DashboardSidebarProps) {
  const { scrollTo } = useScrollTo()

  return (
    <div className={cn("w-full md:w-64 md:shrink-0", className)}>
      <nav className="sticky top-20 flex flex-col space-y-1">
        {items.map((item) => (
          <button
            key={item.id}
            onClick={() => scrollTo(item.id)}
            className="flex items-center rounded-md px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground"
          >
            {item.label}
          </button>
        ))}
      </nav>
    </div>
  )
}
