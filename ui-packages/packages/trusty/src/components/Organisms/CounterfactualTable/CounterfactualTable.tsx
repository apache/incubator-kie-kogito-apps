import React, { useContext, useEffect, useRef, useState } from 'react';
import {
  Td,
  Thead,
  Th,
  Tbody,
  TableComposable,
  Tr
} from '@patternfly/react-table';
import {
  Bullseye,
  Button,
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  EmptyStateVariant,
  Skeleton,
  Title
} from '@patternfly/react-core';
import {
  AngleLeftIcon,
  AngleRightIcon,
  GhostIcon,
  PlusCircleIcon
} from '@patternfly/react-icons';
import { Scrollbars } from 'react-custom-scrollbars';
import {
  CFDispatch,
  CFResult,
  CFSearchInput,
  CFStatus
} from '../../Templates/Counterfactual/Counterfactual';
import CounterfactualInputDomain from '../../Molecules/CounterfactualInputDomain/CounterfactualInputDomain';
import useCFTableSizes from './useCFTableSizes';
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
    'Input',
    'Constraint',
    'Input Value',
    'Counterfactual result'
  ];
  const [rows, setRows] = useState<CFSearchInput[]>(inputs);
  const [areAllRowsSelected, setAreAllRowsSelected] = useState(false);
  const [isScrollDisabled, setIsScrollDisabled] = useState({
    prev: true,
    next: false
  });
  const [displayedResults, setDisplayedResults] = useState(results);
  const [isInputSelectionEnabled, setIsInputSelectionEnabled] = useState<
    boolean
  >();
  const { containerSize, windowSize } = useCFTableSizes({
    containerSelector: '.execution-header'
  });
  const scrollbars = useRef(null);

  useEffect(() => {
    setIsInputSelectionEnabled(status.executionStatus === 'NOT_STARTED');
  }, [status]);

  const slideResults = (action: 'next' | 'prev') => {
    // current scrolling position
    const currentPosition = scrollbars.current.getScrollLeft();
    // window width
    const width = window.innerWidth;
    // size of a result column
    const stepSize = width > 1600 ? 250 : 200;
    let scrollIndex;
    switch (action) {
      case 'prev':
        scrollIndex = Math.abs(Math.round(currentPosition / stepSize) - 1);
        break;
      case 'next':
        scrollIndex = Math.round(currentPosition / stepSize) + 1;
        break;
    }
    const scrollPosition = stepSize * scrollIndex;
    scrollbars.current.view.scroll({
      left: scrollPosition,
      behavior: 'smooth'
    });
  };

  const onScrollUpdate = () => {
    const width = scrollbars.current.getClientWidth();
    const scrollWidth = scrollbars.current.getScrollWidth();
    const currentPosition = scrollbars.current.getScrollLeft();

    console.log('checking scroll enabled/disabled status');
    if (scrollWidth - currentPosition - width < 10) {
      // disabling next button when reaching the right limit (with some tolerance)
      setIsScrollDisabled({ prev: false, next: true });
    } else if (currentPosition < 10) {
      // disabling prev button when at the start (again with tolerance)
      setIsScrollDisabled({ prev: true, next: false });
    } else {
      setIsScrollDisabled({ prev: false, next: false });
    }
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
    setDisplayedResults(results);
  }, [results]);

  const handleScrollbarRendering = (cssClass: string) => {
    return ({ style, ...props }) => {
      return <div style={{ ...style }} {...props} className={cssClass} />;
    };
  };

  return (
    <>
      {windowSize <= 768 && (
        <Bullseye>
          <EmptyState variant={EmptyStateVariant.xs}>
            <EmptyStateIcon icon={GhostIcon} />
            <Title headingLevel="h4" size="md">
              CF implementation for mobile phones
            </Title>
            <EmptyStateBody>workin on it</EmptyStateBody>
          </EmptyState>
        </Bullseye>
      )}
      {windowSize > 768 && (
        <div className="cf-table-outer-container">
          <div className="cf-table-inner-container">
            {containerSize > 0 && (
              <div
                className="cf-table-container cf-table-container--with-results"
                style={{ width: containerSize }}
              >
                <Scrollbars
                  style={{ width: containerSize, height: '100%' }}
                  renderTrackHorizontal={handleScrollbarRendering(
                    'cf-table__scroll-track--horizontal'
                  )}
                  renderThumbHorizontal={handleScrollbarRendering(
                    'cf-table__scroll-thumb--horizontal'
                  )}
                  renderThumbVertical={handleScrollbarRendering(
                    'cf-table__scroll-thumb--vertical'
                  )}
                  renderView={handleScrollbarRendering(
                    'box cf-table__scroll-area'
                  )}
                  onScrollStop={onScrollUpdate}
                  ref={scrollbars}
                >
                  <TableComposable
                    aria-label="Counterfactual Table"
                    className="cf-table cf-table--with-results"
                  >
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

                        <Th
                          info={{
                            tooltip: 'Inputs to the decision model.'
                          }}
                        >
                          {columns[0]}
                        </Th>
                        <Th
                          info={{
                            tooltip:
                              'Limits the data used in the counterfactual.'
                          }}
                        >
                          {columns[1]}
                        </Th>
                        <Th
                          info={{
                            tooltip:
                              'The original input value used by the decision model.'
                          }}
                        >
                          {columns[2]}
                        </Th>
                        {displayedResults.length > 1 && (
                          <Th className="cf-table__slider-cell">
                            <Button
                              variant="link"
                              isInline={true}
                              aria-label="Previous results"
                              className="cf-table__slider-cell__slider"
                              isDisabled={isScrollDisabled.prev}
                              onClick={() => slideResults('prev')}
                            >
                              <AngleLeftIcon />
                            </Button>
                          </Th>
                        )}
                        {displayedResults.length > 0 &&
                          displayedResults[0].map((result, index) => (
                            <Th key={`result ${index}`}>
                              <span>Counterfactual Result</span>
                            </Th>
                          ))}
                        {displayedResults.length > 1 && (
                          <Th className="cf-table__slider-cell">
                            <Button
                              variant="link"
                              isInline={true}
                              aria-label="Next results"
                              className="cf-table__slider-cell__slider"
                              isDisabled={isScrollDisabled.next}
                              onClick={() => slideResults('next')}
                            >
                              <AngleRightIcon />
                            </Button>
                          </Th>
                        )}
                        {displayedResults.length === 0 && (
                          <>
                            <Th key="empty-results-1">
                              <span>Counterfactual Result</span>
                            </Th>
                            <Th key="empty-results-2" />
                          </>
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
                            {!isInputSelectionEnabled && row.domain && (
                              <CounterfactualInputDomain input={row} />
                            )}
                            {isInputSelectionEnabled && canAddConstraint(row) && (
                              <Button
                                variant={'link'}
                                isInline={true}
                                onClick={() =>
                                  onOpenInputDomainEdit(row, rowIndex)
                                }
                                icon={!row.domain && <PlusCircleIcon />}
                                isDisabled={row.isFixed}
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
                            {row.value.toString()}
                          </Td>
                          {displayedResults.length > 1 && (
                            <Td className="cf-table__slider-cell" />
                          )}
                          {displayedResults.length > 0 &&
                            displayedResults[rowIndex].map((value, index) => (
                              <Td
                                key={`${rowIndex}_${index + 4}`}
                                dataLabel={'Counterfactual Result'}
                                className={
                                  value !== row.value
                                    ? 'cf-table__result-value--changed'
                                    : 'cf-table__result-value'
                                }
                              >
                                {value.toString()}
                              </Td>
                            ))}
                          {displayedResults.length > 1 && (
                            <Td className="cf-table__slider-cell" />
                          )}
                          {displayedResults.length === 0 &&
                            status.executionStatus === 'RUNNING' && (
                              <>
                                <Td key="results-loading-1">
                                  <Skeleton
                                    width="85%"
                                    screenreaderText="Loading results"
                                  />
                                </Td>
                                <Td key="results-loading-2">
                                  <Skeleton
                                    width="85%"
                                    screenreaderText="Loading results"
                                  />
                                </Td>
                              </>
                            )}
                          {displayedResults.length === 0 &&
                            status.executionStatus === 'NOT_STARTED' && (
                              <>
                                <Td key="empty-results-1">
                                  No available results
                                </Td>
                                <Td key="empty-results-2" />
                              </>
                            )}
                        </Tr>
                      ))}
                    </Tbody>
                  </TableComposable>
                </Scrollbars>
              </div>
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default CounterfactualTable;
