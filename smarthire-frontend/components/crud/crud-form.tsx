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
import { Checkbox } from "@/components/ui/checkbox" // Certifique-se de ter este import para o Checkbox

export interface CrudFormField {
  name: string;
  label: string;
  type: "text" | "email" | "password" | "textarea" | "file" | "number" | "checkbox";
  description?: string;
  required?: boolean; 
  defaultValue?: any;
}

interface CrudFormProps {
  fields: CrudFormField[]
  submitLabel: string
  onSubmit: (data: any) => void
  isDanger?: boolean
}

export function CrudForm({ fields, submitLabel, onSubmit, isDanger = false }: CrudFormProps) {

  // Crie um objeto para os defaultValues a partir das props 'fields'
  // Iteramos sobre os campos e, se houver um defaultValue, o usamos.
  // Caso contrário, definimos um valor padrão com base no tipo para evitar 'undefined' no Zod.
  const initialDefaultValues = fields.reduce(
    (acc, field) => {
      if (field.defaultValue !== undefined) {
        acc[field.name] = field.defaultValue;
      } else {
        // Define valores padrão se defaultValue não for fornecido,
        // para que o formulário não inicie com `undefined` para campos vazios.
        if (field.type === "number") {
          acc[field.name] = 0;
        } else if (field.type === "checkbox") {
          acc[field.name] = false; // Checkbox geralmente inicia como false
        } else if (field.type !== "file") { // 'file' não tem valor padrão no Zod assim
          acc[field.name] = "";
        }
      }
      return acc;
    },
    {} as Record<string, any> // Use 'any' aqui para flexibilidade de tipos (string, number, boolean)
  );

  // 1. Crie o esquema dinamicamente com base nos campos.
  // Ajuste a validação para `required` e os tipos
  const formSchema = z.object(
    fields.reduce(
      (acc, field) => {
        let schema: any; // Use 'any' temporariamente para flexibilidade do Zod

        if (field.type === "email") {
          schema = z.string().email({ message: "Email inválido" }).or(z.literal(""));
        } else if (field.type === "file") {
          schema = z.any(); // Para arquivos, zod.any() geralmente é suficiente, ou uma validação mais complexa
        } else if (field.type === "number") {
          schema = z.coerce.number({ // coerce.number tenta converter para número
            invalid_type_error: "O valor deve ser um número.",
          })
            .min(0, { message: "O coeficiente de peso deve estar entre 0 e 1." })
            .max(1, { message: "O coeficiente de peso deve estar entre 0 e 1." });
        } else if (field.type === "checkbox") {
          schema = z.boolean(); // Checkbox lida com booleanos
        }
        else {
          schema = z.string();
        }

        // Se o campo for obrigatório, adicione .min(1) ou .nonempty() para strings
        if (field.required) {
          if (field.type === "text" || field.type === "textarea" || field.type === "password") {
            schema = schema.min(1, { message: `${field.label} é obrigatório.` });
          }
          // Para outros tipos, a validação de required já está no schema (ex: number, boolean)
        }

        acc[field.name] = schema;
        return acc;
      },
      {} as Record<string, z.ZodTypeAny>, // Tipo para o reduce
    ),
  );

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    // Passe os defaultValues calculados aqui
    defaultValues: initialDefaultValues,
  });

  function handleSubmit(data: z.infer<typeof formSchema>) {
    onSubmit(data);
    // form.reset(); // Opcional: Remova o reset() se você não quer que o formulário limpe após a submissão em edições
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
                        formField.onChange(e.target.files?.[0] || null);
                      }}
                    />
                  ) : field.type === "number" ? (
                    <Input
                      type="number"
                      min={0}
                      max={1}
                      step={0.01}
                      // Certifique-se de que o valor seja um número para o input type="number"
                      // `value` para input number espera string, então convertemos.
                      // react-hook-form já lida com `valueAsNumber` no register.
                      {...formField}
                      value={formField.value === 0 ? '' : formField.value} // Exibe vazio para 0
                      onChange={(e) => formField.onChange(e.target.value === '' ? null : Number(e.target.value))} // Lida com o input vazio
                      placeholder={field.label}
                    />
                  ) : field.type === "checkbox" ? ( // Renderiza o Checkbox
                    <div className="flex items-center space-x-2">
                      <Checkbox
                        id={field.name}
                        checked={formField.value} // O valor do checkbox é booleano
                        onCheckedChange={formField.onChange} // Use onCheckedChange para Checkbox
                      />
                      <FormLabel htmlFor={field.name} className="!mt-0 cursor-pointer">
                        {field.label}
                      </FormLabel>
                    </div>
                  ) : (
                    <Input
                      type={field.type}
                      {...formField}
                      placeholder={field.label}
                    />
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