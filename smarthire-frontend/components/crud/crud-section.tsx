"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { CrudForm, CrudFormField, FormField } from "@/components/crud/crud-form"
import { CrudTable } from "@/components/crud/crud-table"

interface CrudSectionProps {
  id: string;
  title: string;
  description: string;
  fields: CrudFormField[];
  submitLabel: string;
  onSubmit: (data: any) => void;
  isDanger?: boolean;
  showTable?: boolean;
  tableHeaders?: string[];
  tableData?: string[][];
  onEditClick?: (id: string) => void;
  onDeleteClick?: (id: string) => void;
}

export function CrudSection({
  id,
  title,
  description,
  fields,
  submitLabel,
  onSubmit,
  isDanger = false,
  showTable = false,
  tableHeaders = [],
  tableData = [],
  onEditClick,
  onDeleteClick,
}: CrudSectionProps) {
  return (
    <section id={id} className="scroll-mt-20">
      <Card>
        <CardHeader>
          <CardTitle>{title}</CardTitle>
          <CardDescription>{description}</CardDescription>
        </CardHeader>
        <CardContent>
          <CrudForm fields={fields} submitLabel={submitLabel} onSubmit={onSubmit} isDanger={isDanger} />
          {showTable && tableHeaders.length > 0 && (
            <div className="mt-6">
              <CrudTable 
                headers={tableHeaders} 
                data={tableData} 
                onEditClick={onEditClick}
                onDeleteClick={onDeleteClick}/>
            </div>
          )}
        </CardContent>
      </Card>
    </section>
  )
}
