import React, { useState } from 'react';
import {
  Td,
  Thead,
  Th,
  Tbody,
  TableComposable,
  Tr
} from '@patternfly/react-table';
import { Button } from '@patternfly/react-core';
import { PlusCircleIcon } from '@patternfly/react-icons';

interface CounterfactualTableProps {
  onOpenConstraints: () => void;
}

const CounterfactualTable = (props: CounterfactualTableProps) => {
  const { onOpenConstraints } = props;

  const columns = [
    'Data Type',
    'Input Constraint',
    'Original Input',
    'Counterfactual result'
  ];
  const [rows, setRows] = useState([
    {
      name: 'Credit Score',
      typeRef: 'number',
      value: 738,
      components: null,
      isFixed: true
    },
    {
      name: 'Down Payment',
      typeRef: 'number',
      value: 70000,
      components: null,
      isFixed: true
    },
    {
      name: 'Purchase Price',
      typeRef: 'number',
      value: 34000,
      components: null,
      isFixed: true
    },
    {
      name: 'Monthly Tax Payment',
      typeRef: 'number',
      value: 0.2,
      components: null,
      isFixed: true
    },
    {
      name: 'Monthly Insurance Payment',
      typeRef: 'number',
      value: 0.15,
      components: null,
      isFixed: true
    }
  ]);
  const [areAllRowsSelected, setAreAllRowsSelected] = useState(false);

  const onSelectAll = (event, isSelected) => {
    setAreAllRowsSelected(isSelected);
    setRows(
      rows.map(row => {
        row.isFixed = !isSelected;
        return row;
      })
    );
  };

  const onSelect = (event, isSelected, rowId) => {
    setRows(
      rows.map((row, index) => {
        if (index === rowId) {
          row.isFixed = !isSelected;
        }
        return row;
      })
    );
    if (!isSelected && areAllRowsSelected) {
      setAreAllRowsSelected(false);
    } else if (isSelected && !areAllRowsSelected) {
      let allSelected = true;
      for (let i = 0; i < rows.length; i++) {
        if (i !== rowId) {
          if (rows[i].isFixed === true) {
            allSelected = false;
          }
        }
      }
      if (allSelected) {
        setAreAllRowsSelected(true);
      }
    }
  };
  return (
    <>
      <TableComposable aria-label="Counterfactual Table">
        <Thead>
          <Tr>
            <Th
              select={{
                onSelect: onSelectAll,
                isSelected: areAllRowsSelected
              }}
            />
            <Th width={20}>{columns[0]}</Th>
            <Th width={20}>{columns[1]}</Th>
            <Th width={20}>{columns[2]}</Th>
            <Th width={20}>{columns[3]}</Th>
          </Tr>
        </Thead>
        <Tbody>
          {rows.map((row, rowIndex) => (
            <Tr key={rowIndex}>
              <Td
                key={`${rowIndex}_0`}
                select={{
                  rowIndex,
                  onSelect,
                  isSelected: !row.isFixed
                }}
              />
              <Td key="input name">{row.name}</Td>
              <Td key="input constraint">
                <Button
                  variant={'link'}
                  isInline={true}
                  onClick={onOpenConstraints}
                  icon={<PlusCircleIcon />}
                  isDisabled={row.isFixed}
                >
                  Add constraint
                </Button>
              </Td>
              <Td key="input original value">{row.value}</Td>
              <Td key="result">No available results</Td>
            </Tr>
          ))}
        </Tbody>
      </TableComposable>
    </>
  );
};

export default CounterfactualTable;
