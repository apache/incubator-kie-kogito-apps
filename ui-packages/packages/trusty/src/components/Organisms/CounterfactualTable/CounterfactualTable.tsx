import React, { useState } from 'react';
import {
  Table,
  TableHeader,
  TableBody,
  headerCol,
  IRow,
  ICell
} from '@patternfly/react-table';
import { Button } from '@patternfly/react-core';
import { PlusCircleIcon } from '@patternfly/react-icons';

interface CounterfactualTableProps {
  onOpenConstraints: () => void;
}

const CounterfactualTable = (props: CounterfactualTableProps) => {
  const { onOpenConstraints } = props;
  const columns: (ICell | string)[] = [
    { title: 'Data Type', cellTransforms: [headerCol()] },
    'Input Constraint',
    { title: 'Original input' },
    'Counterfactual result'
  ];
  const [rows, setRows] = useState<IRow[]>([
    {
      cells: [
        <div key={1}>
          <div>one</div>
          <div>two</div>
        </div>,
        <>
          <Button
            key={2}
            variant={'link'}
            isInline={true}
            onClick={onOpenConstraints}
            icon={<PlusCircleIcon />}
          >
            Add constraint
          </Button>
        </>,
        'a',
        'four'
      ]
    },
    {
      cells: ['a', 'two', 'k', 'four']
    },
    {
      cells: ['p', 'two', 'b', 'four']
    }
  ]);

  const onSelect = (
    event: React.FormEvent<HTMLInputElement>,
    isSelected: boolean,
    rowId: number
  ) => {
    let updatedRows;
    if (rowId === -1) {
      updatedRows = rows.map(oneRow => {
        oneRow.selected = isSelected;
        return oneRow;
      });
    } else {
      updatedRows = [...rows];
      updatedRows[rowId].selected = isSelected;
    }
    setRows(updatedRows);
  };

  return (
    <>
      <Table
        onSelect={onSelect}
        canSelectAll={true}
        aria-label="Selectable Table"
        cells={columns}
        rows={rows}
      >
        <TableHeader />
        <TableBody />
      </Table>
    </>
  );
};

export default CounterfactualTable;
