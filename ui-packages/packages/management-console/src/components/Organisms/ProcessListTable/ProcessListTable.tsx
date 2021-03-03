import React, { useEffect, useState } from 'react';
import {
  TableComposable,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  ExpandableRowContent
} from '@patternfly/react-table';
import {
  componentOuiaProps,
  EndpointLink,
  GraphQL,
  ItemDescriptor,
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  OUIAProps
} from '@kogito-apps/common';
import {
  getProcessInstanceDescription,
  ProcessInstanceIconCreator
} from '../../../utils/Utils';
import { Link } from 'react-router-dom';
import Moment from 'react-moment';
import { HistoryIcon } from '@patternfly/react-icons';
import SubProcessTable from '../../Molecules/SubProcessTable/SubProcessTable';
import { Checkbox } from '@patternfly/react-core';
import _ from 'lodash';
import DisablePopup from '../../Molecules/DisablePopup/DisablePopup';
import ProcessInstancesActionsKebab from '../../Atoms/ProcessInstancesActionsKebab/ProcessInstancesActionsKebab';
import ErrorPopover from '../../Atoms/ErrorPopover/ErrorPopover';
import { filterType } from '../../Molecules/ProcessListToolbar/ProcessListToolbar';

interface IOwnProps {
  initData: GraphQL.GetProcessInstancesQuery;
  setInitData: React.Dispatch<
    React.SetStateAction<GraphQL.GetProcessInstancesQuery>
  >;
  loading: boolean;
  filters: filterType;
  expanded: { [key: number]: boolean };
  setExpanded: React.Dispatch<React.SetStateAction<{ [key: number]: boolean }>>;
  setSelectedInstances: React.Dispatch<
    React.SetStateAction<GraphQL.ProcessInstance[]>
  >;
  selectedInstances: GraphQL.ProcessInstance[];
  setSelectableInstances: React.Dispatch<React.SetStateAction<number>>;
  setIsAllChecked: (isAllChecked: boolean) => void;
  selectableInstances: number;
}

interface RowPairType {
  id: string;
  parent: JSX.Element[];
  child: string[];
  noPadding?: boolean;
}

const ProcessListTable: React.FC<IOwnProps & OUIAProps> = ({
  initData,
  loading,
  filters,
  setExpanded,
  expanded,
  setInitData,
  setSelectedInstances,
  selectedInstances,
  setSelectableInstances,
  setIsAllChecked,
  selectableInstances,
  ouiaId,
  ouiaSafe
}) => {
  const [rowPairs, setRowPairs] = useState<RowPairType[]>([]);
  const columns: string[] = ['Id', 'Status', 'Created', 'Last update', ''];
  const numColumns: number = columns.length;
  const currentPage = { prev: location.pathname };
  window.localStorage.setItem('state', JSON.stringify(currentPage));
  useEffect(() => {
    const tempRowPairs = [];
    if (!loading && Object.keys(initData).length !== 0) {
      initData.ProcessInstances.forEach(
        (
          processInstance: GraphQL.ProcessInstance & { isSelected?: boolean }
        ) => {
          tempRowPairs.push({
            id: processInstance.id,
            parent: [
              <>
                {processInstance.addons.includes('process-management') &&
                processInstance.serviceUrl !== null ? (
                  <Checkbox
                    isChecked={processInstance.isSelected}
                    onChange={() => checkBoxSelect(processInstance)}
                    aria-label="process-list-checkbox"
                    id={`checkbox-${processInstance.id}`}
                    name={`checkbox-${processInstance.id}`}
                  />
                ) : (
                  <DisablePopup
                    processInstanceData={processInstance}
                    component={
                      <Checkbox
                        aria-label="process-list-checkbox-disabled"
                        id={`checkbox-${processInstance.id}`}
                        isDisabled={true}
                      />
                    }
                  />
                )}
              </>,
              <>
                <Link
                  to={{
                    pathname: '/Process/' + processInstance.id,
                    state: { filters }
                  }}
                >
                  <div>
                    <strong>
                      <ItemDescriptor
                        itemDescription={getProcessInstanceDescription(
                          processInstance
                        )}
                      />
                    </strong>
                  </div>
                </Link>
                <EndpointLink
                  serviceUrl={processInstance.serviceUrl}
                  isLinkShown={false}
                />
              </>,
              <>
                {processInstance.state ===
                GraphQL.ProcessInstanceState.Error ? (
                  <ErrorPopover processInstanceData={processInstance} />
                ) : (
                  ProcessInstanceIconCreator(processInstance.state)
                )}
              </>,
              processInstance.start ? (
                <Moment fromNow>{new Date(`${processInstance.start}`)}</Moment>
              ) : (
                ''
              ),
              processInstance.lastUpdate ? (
                <span>
                  <HistoryIcon className="pf-u-mr-sm" /> Updated
                  <Moment fromNow>
                    {new Date(`${processInstance.lastUpdate}`)}
                  </Moment>
                </span>
              ) : (
                ''
              ),
              <ProcessInstancesActionsKebab
                processInstance={processInstance}
                key={processInstance.id}
              />
            ],
            child: [processInstance.id]
          });
        }
      );
      setRowPairs(tempRowPairs);
    }
  }, [initData]);

  const LoadChild = (
    parentId: string,
    parentIndex: number
  ): JSX.Element | null => {
    if (!expanded[parentIndex]) {
      return null;
    } else {
      return (
        <SubProcessTable
          parentProcessId={parentId}
          filters={filters}
          initData={initData}
          setInitData={setInitData}
          setSelectedInstances={setSelectedInstances}
          selectedInstances={selectedInstances}
          setSelectableInstances={setSelectableInstances}
        />
      );
    }
  };

  const checkBoxSelect = (
    processInstance: GraphQL.ProcessInstance & { isSelected?: boolean }
  ): void => {
    const clonedInitData = _.cloneDeep(initData);
    clonedInitData.ProcessInstances.forEach(
      (instance: GraphQL.ProcessInstance & { isSelected?: boolean }) => {
        if (processInstance.id === instance.id) {
          if (instance.isSelected) {
            instance.isSelected = false;
            setSelectedInstances(
              selectedInstances.filter(
                selectedInstance => selectedInstance.id !== instance.id
              )
            );
          } else {
            instance.isSelected = true;
            setSelectedInstances([...selectedInstances, instance]);
          }
        }
      }
    );
    setInitData(clonedInitData);
  };

  const onToggle = (pairIndex: number, pair: RowPairType): void => {
    setExpanded({
      ...expanded,
      [pairIndex]: !expanded[pairIndex]
    });

    if (expanded[pairIndex]) {
      const processInstance =
        !loading &&
        !_.isEmpty(initData) &&
        initData.ProcessInstances.find(instance => instance.id === pair.id);
      !_.isEmpty(processInstance['childProcessInstances']) &&
        processInstance['childProcessInstances'].forEach(
          (
            childInstance: GraphQL.ProcessInstance & {
              isOpen?: boolean;
              isSelected?: boolean;
            }
          ) => {
            if (childInstance.isSelected) {
              const index = selectedInstances.findIndex(
                selectedInstance => selectedInstance.id === childInstance.id
              );
              if (index !== -1) {
                selectedInstances.splice(index, 1);
              }
            }
          }
        );
      !loading &&
        !_.isEmpty(initData) &&
        initData.ProcessInstances.forEach(
          (instance: GraphQL.ProcessInstance & { isOpen?: boolean }) => {
            if (processInstance.id === instance.id) {
              instance.isOpen = false;
              instance.childProcessInstances.forEach(child => {
                if (
                  instance.serviceUrl &&
                  instance.addons.includes('process-management')
                ) {
                  setSelectableInstances(prev => prev - 1);
                }
              });
            }
          }
        );
    } else {
      const processInstance =
        !loading &&
        !_.isEmpty(initData) &&
        initData.ProcessInstances.find(instance => instance.id === pair.id);
      !loading &&
        !_.isEmpty(initData) &&
        initData.ProcessInstances.forEach(
          (instance: GraphQL.ProcessInstance & { isOpen?: boolean }) => {
            if (processInstance.id === instance.id) {
              instance.isOpen = true;
            }
          }
        );
    }
    if (
      selectedInstances.length === selectableInstances &&
      selectableInstances !== 0
    ) {
      setIsAllChecked(true);
    } else {
      setIsAllChecked(false);
    }
  };

  return (
    <React.Fragment>
      <TableComposable
        aria-label="Process List Table"
        variant={'compact'}
        {...componentOuiaProps(ouiaId, 'process-list-table', ouiaSafe)}
      >
        <Thead>
          <Tr>
            <Th
              style={{
                width: '72px'
              }}
            />
            <Th
              style={{
                width: '86px'
              }}
            />
            <Th>{columns[0]}</Th>
            <Th>{columns[1]}</Th>
            <Th>{columns[2]}</Th>
            <Th>{columns[3]}</Th>
            <Th>{columns[4]}</Th>
            <Th
              style={{
                width: '188px'
              }}
            />
          </Tr>
        </Thead>
        {!loading && initData && rowPairs.length > 0 ? (
          rowPairs.map((pair, pairIndex) => {
            const parentRow = (
              <Tr key={`${pairIndex}-parent`}>
                <Td
                  key={`${pairIndex}-parent-0`}
                  expand={{
                    rowIndex: pairIndex,
                    isExpanded: expanded[pairIndex],
                    onToggle: event => onToggle(pairIndex, pair)
                  }}
                />
                {pair.parent.map((cell, cellIndex) => (
                  <Td
                    key={`${pairIndex}-parent-${++cellIndex}`}
                    dataLabel={columns[cellIndex]}
                  >
                    {cell}
                  </Td>
                ))}
              </Tr>
            );
            const childRow = (
              <Tr
                key={`${pairIndex}-child`}
                isExpanded={expanded[pairIndex] === true}
              >
                <Td key={`${pairIndex}-child-0`} />
                {rowPairs[pairIndex].child.map((cell, cellIndex) => (
                  <Td
                    key={`${pairIndex}-child-${++cellIndex}`}
                    dataLabel={columns[cellIndex]}
                    noPadding={rowPairs[pairIndex].noPadding}
                    colSpan={numColumns}
                  >
                    <ExpandableRowContent>
                      {LoadChild(cell, pairIndex)}
                    </ExpandableRowContent>
                  </Td>
                ))}
              </Tr>
            );
            return (
              <Tbody key={pairIndex} isExpanded={expanded[pairIndex] === true}>
                {parentRow}
                {childRow}
              </Tbody>
            );
          })
        ) : (
          <tbody>
            <Tr>
              <Td colSpan={7}>
                <>
                  {loading && (
                    <KogitoSpinner
                      spinnerText={'Loading process instances...'}
                    />
                  )}
                  {!loading && rowPairs.length === 0 && (
                    <KogitoEmptyState
                      type={KogitoEmptyStateType.Search}
                      title="No results found"
                      body="Try using different filters"
                    />
                  )}
                </>
              </Td>
            </Tr>
          </tbody>
        )}
      </TableComposable>
    </React.Fragment>
  );
};

export default ProcessListTable;
