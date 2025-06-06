
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { Brain, Edit, Trash } from "lucide-react"

interface CrudTableProps {
  headers: string[];
  data: string[][];
  onAnalyzeClick?: (id: string) => void;
  onEditClick?: (id: string) => void;
  onDeleteClick?: (id: string) => void;
  idColumnIndex?: number;
  customRenderers?: ((row: string[], rowIndex: number) => React.ReactNode)[];
}

export function CrudTable({
  headers,
  data,
  onEditClick,
  onDeleteClick,
  onAnalyzeClick,
  idColumnIndex,
  customRenderers = [],
}: CrudTableProps) {
  return (
    <div className="rounded-md border">
      <Table>
        <TableHeader>
          <TableRow>
            {headers.map((header, index) => (
              <TableHead key={index}>{header}</TableHead>
            ))}
            <TableHead className="text-center">Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {data.map((row, rowIndex) => {
            const actionId = row[idColumnIndex ?? 1];

            return (
              <TableRow key={rowIndex}>
                {row.map((cell, cellIndex) => (
                  <TableCell key={cellIndex}>{cell}</TableCell>
                ))}
                {customRenderers.map((renderer, customIndex) => (
                  <TableCell key={`custom-${customIndex}`}>
                    {renderer(row, rowIndex)}
                  </TableCell>
                ))}

                <TableCell className="flex gap-2 justify-center">
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => onAnalyzeClick && onAnalyzeClick(actionId)} 
                  >
                    <Brain className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => onEditClick && onEditClick(actionId)} 
                  >
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => onDeleteClick && onDeleteClick(actionId)}
                  >
                    <Trash className="h-4 w-4" />
                  </Button>
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </div>
  );
}
