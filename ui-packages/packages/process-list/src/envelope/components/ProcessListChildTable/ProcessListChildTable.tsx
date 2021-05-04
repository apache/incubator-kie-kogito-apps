/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { ProcessListDriver } from '../../../api';
import { IRow, Table, TableBody, TableHeader } from '@patternfly/react-table';
import React, { useEffect, useState } from 'react';
import { ProcessInstance } from '@kogito-apps/management-console-shared';
import _ from 'lodash';
import {
  ServerErrors,
  KogitoSpinner,
  ItemDescriptor,
  EndpointLink,
  KogitoEmptyState,
  KogitoEmptyStateType,
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/components-common';
import {
  getProcessInstanceDescription,
  ProcessInstanceIconCreator
} from '../utils/ProcessListUtils';
import { HistoryIcon } from '@patternfly/react-icons';
import Moment from 'react-moment';
import ProcessListActionsKebab from '../ProcessListActionsKebab/ProcessListActionsKebab';
import { Checkbox } from '@patternfly/react-core';
import DisablePopup from '../DisablePopup/DisablePopup';
export interface ProcessListChildTableProps {
  parentProcessId: string;
  processInstances: ProcessInstance[];
  setProcessInstances: React.Dispatch<React.SetStateAction<ProcessInstance[]>>;
  selectedInstances: ProcessInstance[];
  setSelectedInstances: React.Dispatch<React.SetStateAction<ProcessInstance[]>>;
  driver: ProcessListDriver;
  onSkipClick: (processInstance: ProcessInstance) => Promise<void>;
  onRetryClick: (processInstance: ProcessInstance) => Promise<void>;
  onAbortClick: (processInstance: ProcessInstance) => Promise<void>;
  setSelectableInstances: React.Dispatch<React.SetStateAction<number>>;
}
const ProcessListChildTable: React.FC<ProcessListChildTableProps &
  OUIAProps> = ({
  parentProcessId,
  selectedInstances,
  setSelectedInstances,
  processInstances,
  setProcessInstances,
  driver,
  onSkipClick,
  onRetryClick,
  onAbortClick,
  setSelectableInstances,
  ouiaId,
  ouiaSafe
}) => {
  const [rows, setRows] = useState<(IRow | string[])[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [showNoDataEmptyState, setShowNoDataEmptyState] = useState<boolean>(
    false
  );
  const [error, setError] = useState<string>(undefined);
  const columns = [
    {
      title: ''
    },
    {
      title: 'Id'
    },
    {
      title: 'Status'
    },
    {
      title: 'Created'
    },
    {
      title: 'Last update'
    },
    {
      title: ''
    }
  ];

  const checkBoxSelect = (processInstance: ProcessInstance): void => {
    const clonedProcessInstances = [...processInstances];
    clonedProcessInstances.forEach((instance: ProcessInstance) => {
      if (instance.id === parentProcessId) {
        instance.childProcessInstances.forEach(
          (childInstance: ProcessInstance) => {
            if (childInstance.id === processInstance.id) {
              if (childInstance.isSelected) {
                childInstance.isSelected = false;
                setSelectedInstances(
                  selectedInstances.filter(
                    selectedInstance => selectedInstance.id !== childInstance.id
                  )
                );
              } else {
                childInstance.isSelected = true;
                setSelectedInstances([...selectedInstances, childInstance]);
              }
            }
          }
        );
      }
    });
    setProcessInstances(clonedProcessInstances);
  };

  const createRows = (processInstances: ProcessInstance[]): void => {
    if (!_.isEmpty(processInstances)) {
      const tempRows = [];
      processInstances.forEach((child: ProcessInstance) => {
        tempRows.push({
          cells: [
            {
              title: (
                <>
                  {child.addons.includes('process-management') &&
                  child.serviceUrl !== null ? (
                    <Checkbox
                      isChecked={child.isSelected}
                      onChange={() => {
                        checkBoxSelect(child);
                      }}
                      aria-label="process-list-checkbox"
                      id={`checkbox-${child.id}`}
                      name={`checkbox-${child.id}`}
                    />
                  ) : (
                    <DisablePopup
                      processInstanceData={child}
                      component={
                        <Checkbox
                          aria-label="process-list-checkbox-disabled"
                          id={`checkbox-${child.id}`}
                          isDisabled={true}
                        />
                      }
                    />
                  )}
                </>
              )
            },
            {
              title: (
                <>
                  <div>
                    <strong>
                      <ItemDescriptor
                        itemDescription={getProcessInstanceDescription(child)}
                      />
                    </strong>
                  </div>
                  <EndpointLink
                    serviceUrl={child.serviceUrl}
                    isLinkShown={false}
                  />
                </>
              )
            },
            {
              title: ProcessInstanceIconCreator(child.state)
            },
            {
              title: child.start ? (
                <Moment fromNow>{new Date(`${child.start}`)}</Moment>
              ) : (
                ''
              )
            },
            {
              title: child.lastUpdate ? (
                <span>
                  {' '}
                  <HistoryIcon className="pf-u-mr-sm" /> Updated{' '}
                  <Moment fromNow>{new Date(`${child.lastUpdate}`)}</Moment>
                </span>
              ) : (
                ''
              )
            },
            {
              title: (
                <ProcessListActionsKebab
                  processInstance={child}
                  onSkipClick={onSkipClick}
                  onRetryClick={onRetryClick}
                  onAbortClick={onAbortClick}
                  key={child.id}
                />
              )
            }
          ]
        });
      });
      setRows(tempRows);
      setShowNoDataEmptyState(false);
    } else {
      setShowNoDataEmptyState(true);
    }
  };

  const getChildProcessInstances = async (): Promise<void> => {
    try {
      setIsLoading(true);
      const response = await driver.getChildProcessesQuery(parentProcessId);
      const clonedProcessInstances = _.cloneDeep(processInstances);
      clonedProcessInstances.forEach((processInstance: ProcessInstance) => {
        if (processInstance.id === parentProcessId) {
          response.forEach((child: ProcessInstance) => {
            child.isSelected = false;
            if (
              child.serviceUrl &&
              child.addons.includes('process-management')
            ) {
              setSelectableInstances(prev => prev + 1);
            }
          });
          processInstance.childProcessInstances = response;
        }
      });
      setProcessInstances(clonedProcessInstances);
    } catch (error) {
      setError(error);
    } finally {
      setIsLoading(false);
    }
  };
  useEffect(() => {
    if (processInstances.length > 0) {
      const processInstance = processInstances.find(
        (processInstance: ProcessInstance) =>
          processInstance.id === parentProcessId
      );
      createRows(processInstance.childProcessInstances);
    }
  }, [processInstances]);

  useEffect(() => {
    getChildProcessInstances();
  }, []);

  if (isLoading) {
    return <KogitoSpinner spinnerText={'Loading child instances...'} />;
  }

  if (error) {
    return <ServerErrors error={error} variant="large" />;
  }

  if (!isLoading && showNoDataEmptyState) {
    return (
      <KogitoEmptyState
        type={KogitoEmptyStateType.Info}
        title="No child process instances"
        body="This process has no related sub processes"
      />
    );
  }

  return (
    <Table
      aria-label="Process List Child Table"
      cells={columns}
      rows={rows}
      variant={'compact'}
      className="kogito-management-console__compact-table"
      {...componentOuiaProps(
        ouiaId,
        'process-list-child-table',
        ouiaSafe ? ouiaSafe : !isLoading
      )}
    >
      <TableHeader />
      <TableBody />
    </Table>
  );
};

export default ProcessListChildTable;
