import React, { useContext, useEffect, useState } from 'react';
import {
  Td,
  Thead,
  Th,
  Tbody,
  TableComposable,
  Tr
} from '@patternfly/react-table';
import { Button, Skeleton } from '@patternfly/react-core';
import {
  AngleLeftIcon,
  AngleRightIcon,
  PlusCircleIcon
} from '@patternfly/react-icons';
import {
  CFDispatch,
  CFResult,
  CFSearchInput,
  CFStatus
} from '../../Templates/Counterfactual/Counterfactual';
import CounterfactualInputDomain from '../../Molecules/CounterfactualInputDomain/CounterfactualInputDomain';
import './CounterfactualTable.scss';

interface CounterfactualTableProps {
  inputs: CFSearchInput[];
  results: CFResult[];
  status: CFStatus;
  onOpenInputDomainEdit: (input: CFSearchInput, inputIndex: number) => void;
}

const CounterfactualTable = (props: CounterfactualTableProps) => {
  const { inputs, results, status, onOpenInputDomainEdit } = props;
  const dispatch = useContext(CFDispatch);
  const columns = [
    'Data Type',
    'Input Constraint',
    'Original Input',
    'Counterfactual result'
  ];
  const [rows, setRows] = useState<CFSearchInput[]>(inputs);
  const [areAllRowsSelected, setAreAllRowsSelected] = useState(false);
  // displayed results are the visible portion of cf results
  const [displayedResultsIndex, setDisplayedResultsIndex] = useState(0);
  const [displayedResults, setDisplayedResults] = useState(
    results.map(result => result.slice(0, 2))
  );

  const [isInputSelectionEnabled, setIsInputSelectionEnabled] = useState<
    boolean
  >();

  useEffect(() => {
    setIsInputSelectionEnabled(status.executionStatus === 'NOT_STARTED');
  }, [status]);

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
      results.map(result => result.slice(newIndex, newIndex + 2))
    );
  };

  const onSelectAll = (event, isSelected: boolean) => {
    dispatch({
      type: 'toggleAllInputs',
      payload: { selected: isSelected }
    });
  };

  const onSelect = (event, isSelected, rowId) => {
    dispatch({
      type: 'toggleInput',
      payload: { searchInputIndex: rowId }
    });
  };

  const canAddConstraint = (input: CFSearchInput) => {
    return input.typeRef !== 'boolean';
  };

  useEffect(() => {
    setRows(inputs);
    setAreAllRowsSelected(inputs.find(input => input.isFixed) === undefined);
  }, [inputs]);

  useEffect(() => {
    setDisplayedResults(
      results.length
        ? results.map(result =>
            result.slice(displayedResultsIndex, displayedResultsIndex + 2)
          )
        : []
    );
  }, [results, displayedResultsIndex]);

  return (
    <>
      <TableComposable aria-label="Counterfactual Table" className="cf-table">
        <Thead>
          <Tr>
            {isInputSelectionEnabled ? (
              <Th
                select={{
                  onSelect: onSelectAll,
                  isSelected: areAllRowsSelected
                }}
              />
            ) : (
              <Th />
            )}

            <Th width={20}>{columns[0]}</Th>
            <Th width={20}>{columns[1]}</Th>
            <Th width={20}>{columns[2]}</Th>
            {displayedResults.length > 0 &&
              displayedResults[0].map((result, index) => (
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
                        results[0] &&
                        results[0].length ===
                          displayedResultsIndex + displayedResults[0].length
                      }
                      onClick={() => slideResults('next')}
                    >
                      <AngleRightIcon />
                    </Button>
                  )}
                </Th>
              ))}
            {displayedResults.length === 0 && (
              <Th width={20} key="results">
                <span>Counterfactual Result</span>
              </Th>
            )}
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
                  isSelected: !row.isFixed,
                  disable: !isInputSelectionEnabled
                }}
              />
              <Td key={`${rowIndex}_1`} dataLabel={columns[0]}>
                {row.name}
              </Td>
              <Td key={`${rowIndex}_2`} dataLabel={columns[1]}>
                {canAddConstraint(row) && (
                  <Button
                    variant={'link'}
                    isInline={true}
                    onClick={() => onOpenInputDomainEdit(row, rowIndex)}
                    icon={!row.domain && <PlusCircleIcon />}
                    isDisabled={row.isFixed || !isInputSelectionEnabled}
                  >
                    {row.domain ? (
                      <CounterfactualInputDomain input={row} />
                    ) : (
                      <>Constraint</>
                    )}
                  </Button>
                )}
              </Td>
              <Td key={`${rowIndex}_3`} dataLabel={columns[2]}>
                {row.value}
              </Td>
              {displayedResults.length > 0 &&
                displayedResults[rowIndex].map((value, index) => (
                  <Td
                    key={`${rowIndex}_${index + 4}`}
                    dataLabel={'Counterfactual Result'}
                  >
                    {value}
                  </Td>
                ))}
              {displayedResults.length === 0 &&
                status.executionStatus === 'RUNNING' && (
                  <Td key="results loading">
                    <Skeleton width="50%" screenreaderText="Loading results" />
                  </Td>
                )}
              {displayedResults.length === 0 &&
                status.executionStatus === 'NOT_STARTED' && (
                  <Td key="results loading">No available results</Td>
                )}
            </Tr>
          ))}
        </Tbody>
      </TableComposable>
    </>
  );
};

export default CounterfactualTable;
