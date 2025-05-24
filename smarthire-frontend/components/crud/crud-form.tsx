"use client"

import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import * as z from "zod"
import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField as FormFieldComponent,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"

export interface CrudFormField {
  name: string;
  label: string;
  type: "text" | "email" | "password" | "textarea" | "file" | "number";
  description?: string;
}

interface CrudFormProps {
  fields: CrudFormField[]
  submitLabel: string
  onSubmit: (data: any) => void
  isDanger?: boolean
}

export function CrudForm({ fields, submitLabel, onSubmit, isDanger = false }: CrudFormProps) {
  // Dynamically create schema based on fields
  const formSchema = z.object(
    fields.reduce(
      (acc, field) => {
        if (field.type === "email") {
          acc[field.name] = z.string().email({ message: "Email inválido" }).default("");
        } else if (field.type === "file") {
          acc[field.name] = z.any().default("");
        } else if (field.type === "number") {
          acc[field.name] = z.coerce.number({
            invalid_type_error: "Erro: Número inválido",
          })
          .min(0, {message: "O coeficiente de peso deve estar entre 0 e 1."})
          .max(1, {message: "O coeficiente de peso deve estar entre 0 e 1."}).default(0);
        } else {
          acc[field.name] = z.string().default("");
        }
        return acc
      },
      {} as Record<string, any>,
    ),
  )

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: fields.reduce(
      (acc, field) => {
        if (field.type !== "file") {
          acc[field.name] = ""
        }
        return acc
      },
      {} as Record<string, string>,
    ),
  })

  function handleSubmit(data: z.infer<typeof formSchema>) {
    onSubmit(data)
    form.reset()
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-6">
        {fields.map((field) => (
          <FormFieldComponent
            key={field.name}
            control={form.control}
            name={field.name}
            render={({ field: formField }) => (
              <FormItem>
                <FormLabel>{field.label}</FormLabel>
                <FormControl>
                  {field.type === "textarea" ? (
                    <Textarea {...formField} placeholder={field.label} />
                  ) : field.type === "file" ? (
                    <Input
                      type="file"
                      onChange={(e) => {
                        formField.onChange(e.target.files?.[0] || null)
                      }}
                    />
                  ) : field.type === "number" ? (
                    <Input 
                      min={0}
                      max={1}
                      step={0.01}
                      {...formField} 
                      placeholder={field.label} />
                  ) : (
                    <Input 
                      type={field.type} 
                      {...formField} 
                      placeholder={field.label} />
                  )}
                </FormControl>
                {field.description && <FormDescription>{field.description}</FormDescription>}
                <FormMessage />
              </FormItem>
            )}
          />
        ))}
        <Button type="submit" variant={isDanger ? "destructive" : "default"}>
          {submitLabel}
        </Button>
      </form>
    </Form>
  )
}

export { FormFieldComponent as FormField }
