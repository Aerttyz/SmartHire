"use client";

import { CrudForm, CrudFormField } from "@/components/crud/crud-form";
import { CrudTable } from "@/components/crud/crud-table";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

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
  onAnalyzeClick?: (id: string) => void;
  onEditClick?: (id: string) => void;
  onDeleteClick?: (id: string) => void;
  customRenderers?: ((row: string[], rowIndex: number) => React.ReactNode)[];
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
  onAnalyzeClick,
  onEditClick,
  onDeleteClick,
  customRenderers = [],
}: CrudSectionProps) {
  return (
    <section id={id} className="scroll-mt-20">
      <Card>
        <CardHeader>
          <CardTitle>{title}</CardTitle>
          <CardDescription>{description}</CardDescription>
        </CardHeader>
        <CardContent>
          <CrudForm
            fields={fields}
            submitLabel={submitLabel}
            onSubmit={onSubmit}
            isDanger={isDanger}
          />
          {showTable && tableHeaders.length > 0 && (
            <div className="mt-6">
              <CrudTable 
                headers={tableHeaders} 
                data={tableData} 
                onAnalyzeClick={onAnalyzeClick}
                onEditClick={onEditClick}
                onDeleteClick={onDeleteClick}
                customRenderers={customRenderers}
              />
            </div>
          )}
        </CardContent>
      </Card>
    </section>
  );
}
