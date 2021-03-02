import {
  componentOuiaProps,
  EndpointLink,
  GraphQL,
  ItemDescriptor,
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  OUIAProps,
  ServerErrors
} from '@kogito-apps/common';
import { HistoryIcon } from '@patternfly/react-icons';
import { IRow, Table, TableBody, TableHeader } from '@patternfly/react-table';
import {
  checkProcessInstanceState,
  getProcessInstanceDescription,
  ProcessInstanceIconCreator
} from '../../../utils/Utils';
import React, { useEffect, useState } from 'react';
import Moment from 'react-moment';
import { Link } from 'react-router-dom';
import _ from 'lodash';
import DisablePopup from '../DisablePopup/DisablePopup';
import { Checkbox } from '@patternfly/react-core';
import ProcessInstancesActionsKebab from '../../Atoms/ProcessInstancesActionsKebab/ProcessInstancesActionsKebab';
import ErrorPopover from '../../Atoms/ErrorPopover/ErrorPopover';
import { filterType } from '../ProcessListToolbar/ProcessListToolbar';
import './SubProcessTable.css';
interface IOwnProps {
  parentProcessId: string;
  filters: filterType;
  initData: GraphQL.GetProcessInstancesQuery;
  setInitData: React.Dispatch<
    React.SetStateAction<GraphQL.GetProcessInstancesQuery>
  >;
  setSelectedInstances: React.Dispatch<
    React.SetStateAction<GraphQL.ProcessInstance[]>
  >;
  selectedInstances: GraphQL.ProcessInstance[];
  setSelectableInstances: React.Dispatch<React.SetStateAction<number>>;
}
const SubProcessTable: React.FC<IOwnProps & OUIAProps> = ({
  parentProcessId,
  filters,
  initData,
  setInitData,
  setSelectedInstances,
  setSelectableInstances,
  selectedInstances,
  ouiaId,
  ouiaSafe
}) => {
  const [rows, setRows] = useState<(IRow | string[])[]>([]);
  const { loading, error, data } = GraphQL.useGetChildInstancesQuery({
    variables: {
      rootProcessInstanceId: parentProcessId
    }
  });
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

  useEffect(() => {
    if (!loading && data) {
      const clonedInitData = _.cloneDeep(initData);
      !_.isEmpty(clonedInitData) &&
        clonedInitData.ProcessInstances.forEach(processInstance => {
          if (processInstance.id === parentProcessId) {
            data.ProcessInstances.forEach(
              (
                processInstance: GraphQL.ProcessInstance & {
                  isSelected: boolean;
                }
              ) => {
                processInstance.isSelected = false;
              }
            );
            processInstance.childProcessInstances = data.ProcessInstances;
          }
        });
      data.ProcessInstances.forEach(
        (
          instance: GraphQL.ProcessInstance & {
            isSelected: boolean;
          }
        ) => {
          instance.isSelected = false;
          if (!checkProcessInstanceState(instance)) {
            setSelectableInstances(prev => prev + 1);
          }
        }
      );
      setInitData(clonedInitData);
    }
  }, [data]);

  useEffect(() => {
    const tempRows = [];
    const processInstance =
      !_.isEmpty(initData) &&
      initData.ProcessInstances.find(
        processInstance => processInstance.id === parentProcessId
      );
    if (
      !_.isEmpty(processInstance) &&
      processInstance['childProcessInstances'] &&
      processInstance['childProcessInstances'].length > 0
    ) {
      processInstance['childProcessInstances'].forEach(
        (child: GraphQL.ProcessInstance & { isSelected: boolean }) => {
          tempRows.push({
            cells: [
              {
                title: (
                  <>
                    {child.addons.includes('process-management') &&
                    child.serviceUrl !== null ? (
                      <Checkbox
                        isChecked={child.isSelected}
                        onChange={() => checkBoxSelect(child)}
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
                    <Link
                      to={{
                        pathname: '/Process/' + child.id,
                        state: { filters }
                      }}
                    >
                      <div>
                        <strong>
                          <ItemDescriptor
                            itemDescription={getProcessInstanceDescription(
                              child
                            )}
                          />
                        </strong>
                      </div>
                    </Link>
                    <EndpointLink
                      serviceUrl={child.serviceUrl}
                      isLinkShown={false}
                    />
                  </>
                )
              },
              {
                title: (
                  <>
                    {child.state === GraphQL.ProcessInstanceState.Error ? (
                      <ErrorPopover processInstanceData={child} />
                    ) : (
                      ProcessInstanceIconCreator(child.state)
                    )}
                  </>
                )
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
                title: <ProcessInstancesActionsKebab processInstance={child} />
              }
            ]
          });
        }
      );
      setRows(tempRows);
    }
  }, [initData]);

  const checkBoxSelect = (processInstance: GraphQL.ProcessInstance): void => {
    const clonedInitData = _.cloneDeep(initData);
    clonedInitData.ProcessInstances.forEach(instance => {
      if (instance.id === parentProcessId) {
        instance.childProcessInstances.forEach(childInstance => {
          if (childInstance.id === processInstance.id) {
            if (!checkProcessInstanceState(childInstance)) {
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
        });
      }
    });
    setInitData(clonedInitData);
  };

  if (loading) {
    return <KogitoSpinner spinnerText={'Loading child instances...'} />;
  }
  if (error) {
    return <ServerErrors error={error} variant="large" />;
  }
  if (!loading && data && data.ProcessInstances.length === 0) {
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
      aria-label="Sub Process Table"
      cells={columns}
      rows={rows}
      className="kogito-management-console__compact-table"
      {...componentOuiaProps(ouiaId, 'sub-process-table', ouiaSafe)}
    >
      <TableHeader />
      <TableBody />
    </Table>
  );
};

export default SubProcessTable;
