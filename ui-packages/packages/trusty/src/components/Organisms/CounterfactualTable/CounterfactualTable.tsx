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
import {
  AngleLeftIcon,
  AngleRightIcon,
  PlusCircleIcon
} from '@patternfly/react-icons';
import { CFSearchInput } from '../../Templates/Counterfactual/Counterfactual';
import './CounterfactualTable.scss';

interface CounterfactualTableProps {
  onOpenConstraints: (input: CFSearchInput) => void;
}

const CounterfactualTable = (props: CounterfactualTableProps) => {
  const { onOpenConstraints } = props;

  const columns = [
    'Data Type',
    'Input Constraint',
    'Original Input',
    'Counterfactual result'
  ];
  const [rows, setRows] = useState<CFSearchInput[]>([
    {
      name: 'Credit Score',
      typeRef: 'number',
      value: 738,
      isFixed: true
    },
    {
      name: 'Down Payment',
      typeRef: 'number',
      value: 70000,
      isFixed: true
    },
    {
      name: 'Purchase Price',
      typeRef: 'number',
      value: 34000,
      isFixed: true
    },
    {
      name: 'Monthly Tax Payment',
      typeRef: 'number',
      value: 0.2,
      isFixed: true
    },
    {
      name: 'Monthly Insurance Payment',
      typeRef: 'number',
      value: 0.15,
      isFixed: true
    }
  ] as CFSearchInput[]);

  const [areAllRowsSelected, setAreAllRowsSelected] = useState(false);

  const cfResults: Array<Array<unknown>> = [
    [33, 44, 56, 43],
    [12, 4, 3, 2],
    [1000, 1300, 1250, 1650],
    [500, 540, 420, 502],
    ['ALFA', 'BETA', 'GAMMA', 'DELTA']
  ];

  const [displayedResultsIndex, setDisplayedResultsIndex] = useState(0);
  const [displayedResults, setDisplayedResults] = useState(
    cfResults.map(result => result.slice(0, 2))
  );

  const slideResults = (action: 'next' | 'prev') => {
    let newIndex;
    switch (action) {
      case 'prev':
        newIndex = displayedResultsIndex - 1;
        break;
      case 'next':
        newIndex = displayedResultsIndex + 1;
        break;
    }
    setDisplayedResultsIndex(newIndex);
    setDisplayedResults(
      cfResults.map(result => result.slice(newIndex, newIndex + 2))
    );
  };

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
      <TableComposable aria-label="Counterfactual Table" className="cf-table">
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
            {displayedResults[0].map((result, index) => (
              <Th
                width={20}
                key={`result ${index}`}
                className={
                  index === 0
                    ? 'cf-table__result-head--first'
                    : index === displayedResults[0].length - 1
                    ? 'cf-table__result-head--last'
                    : ''
                }
              >
                {index === 0 && (
                  <Button
                    variant="link"
                    isInline={true}
                    aria-label="Previous results"
                    className="cf-table__result-head__slider"
                    isDisabled={displayedResultsIndex === index}
                    onClick={() => slideResults('prev')}
                  >
                    <AngleLeftIcon />
                  </Button>
                )}
                <span>Counterfactual Result</span>
                {index === displayedResults[0].length - 1 && (
                  <Button
                    variant="link"
                    isInline={true}
                    aria-label="Next results"
                    className="cf-table__result-head__slider"
                    isDisabled={
                      cfResults[0].length ===
                      displayedResultsIndex + displayedResults[0].length
                    }
                    onClick={() => slideResults('next')}
                  >
                    <AngleRightIcon />
                  </Button>
                )}
              </Th>
            ))}
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
              <Td key={`${rowIndex}_1`}>{row.name}</Td>
              <Td key={`${rowIndex}_2`}>
                <Button
                  variant={'link'}
                  isInline={true}
                  onClick={() => onOpenConstraints(row)}
                  icon={<PlusCircleIcon />}
                  isDisabled={row.isFixed}
                >
                  Add constraint
                </Button>
              </Td>
              <Td key={`${rowIndex}_3`}>{row.value}</Td>
              {displayedResults[rowIndex].map((value, index) => (
                <Td key={`${rowIndex}_${index + 4}`}>{value}</Td>
              ))}
            </Tr>
          ))}
        </Tbody>
      </TableComposable>
    </>
  );
};

export default CounterfactualTable;
