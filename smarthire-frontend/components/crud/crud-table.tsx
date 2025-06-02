import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Edit, Trash } from "lucide-react";

interface CrudTableProps {
  headers: string[];
  data: string[][];
  onEditClick?: (id: string) => void;
  onDeleteClick?: (id: string) => void;
  customRenderers?: ((row: string[], rowIndex: number) => React.ReactNode)[];
}

export function CrudTable({
  headers,
  data,
  onEditClick,
  onDeleteClick,
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
            const vagaId = row[1];

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
                    onClick={() => onEditClick && onEditClick(vagaId)}
                  >
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => onDeleteClick && onDeleteClick(vagaId)}
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
