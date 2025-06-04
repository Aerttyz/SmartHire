import * as React from "react"
import { cn } from "@/lib/utils"

export interface ReadOnlyTextDisplayProps
  extends React.HTMLAttributes<HTMLDivElement> {
  value: string | number | null | undefined;
  placeholder?: string;
}

const ReadOnlyTextDisplay = React.forwardRef<
  HTMLDivElement,
  ReadOnlyTextDisplayProps
>(({ className, value, placeholder = "Nenhum valor fornecido", ...props }, ref) => {
  const displayValue =
    value !== null && value !== undefined && String(value).trim() !== ""
      ? String(value)
      : placeholder;

  const isPlaceholder = displayValue === placeholder;

  return (
    <div
      ref={ref}
      className={cn(
        "min-h-[40px]",
        "w-full",
        "rounded-md",
        "border border-input", 
        "bg-background",      
        "px-3 py-2",          
        "text-sm",            
        
        "whitespace-pre-wrap", 
        "break-words",         
        isPlaceholder ? "text-muted-foreground" : "text-foreground",
        className 
      )}
      {...props} 
    >
      {displayValue}
    </div>
  );
});
ReadOnlyTextDisplay.displayName = "ReadOnlyTextDisplay";

export { ReadOnlyTextDisplay };