/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import {
  Card,
  Grid,
  GridItem,
  PageSection,
  Bullseye,
  Label
} from '@patternfly/react-core';
import React, { useEffect, useState } from 'react';
import UserTaskPageHeader from '../../Molecules/UserTaskPageHeader/UserTaskPageHeader';
import './UserTaskDataTable.css';
import {
  ouiaPageTypeAndObjectId,
  KogitoSpinner,
  DataTableColumn,
  DataTable,
  GraphQL,
  OUIAProps,
  componentOuiaProps
} from '@kogito-apps/common';
import _ from 'lodash';

const UserTaskLoadingComponent = (
  <Bullseye>
    <KogitoSpinner spinnerText="Loading user tasks..." />
  </Bullseye>
);

const stateColumnTransformer = value => {
  // rowDataObj is the original data object to render the row
  if (!value) {
    return null;
  }

  return <Label>{value}</Label>;
};

const UserTaskDataTableContainer: React.FC<OUIAProps> = ({
  ouiaId,
  ouiaSafe
}) => {
  const [sortBy, setSortBy] = useState({});
  const [orderByObj, setOrderByObj] = useState({});

  const {
    loading,
    error,
    data,
    refetch,
    networkStatus
  } = GraphQL.useGetUserTasksByStatesQuery({
    variables: {
      state: ['Ready'], // FIXME: state should not be hard-coded.
      orderBy: { state: GraphQL.OrderBy.Asc }
    },
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true
  });

  // In current implementation, 'state', 'actualOwner', 'description', 'name', 'priority', 'completed', 'started', 'referenceName', and 'lastUpdate' fileds are sortable.
  const columns: DataTableColumn[] = [
    {
      label: 'ProcessId',
      path: '$.processId'
    },
    {
      label: 'Name',
      path: '$.name',
      isSortable: true
    },
    {
      label: 'Priority',
      path: '$.priority'
    },
    {
      label: 'ProcessInstanceId',
      path: '$.processInstanceId'
    },
    {
      label: 'State',
      path: '$.state',
      bodyCellTransformer: stateColumnTransformer,
      isSortable: true
    }
  ];

  useEffect(() => {
    if (!_.isEmpty(orderByObj)) {
      // tslint:disable-next-line:no-floating-promises
      refetch({
        state: ['Ready'], // FIXME: state should not be hard-coded.
        orderBy: orderByObj
      });
    }
  }, [orderByObj]);

  useEffect(() => {
    return ouiaPageTypeAndObjectId('user-tasks');
  });

  const onSorting = (index, direction) => {
    if (index && direction) {
      const sortingColumn = _.last(columns[index].path.split('.'));
      setSortBy({ index, direction }); // This is required by PF4 Table Component
      setOrderByObj(_.set({}, sortingColumn, direction.toUpperCase()));
    }
  };

  return (
    <React.Fragment>
      <div
        {...componentOuiaProps(ouiaId, 'UserTaskDataTableContainer', ouiaSafe)}
      >
        <UserTaskPageHeader />
        <PageSection>
          <Grid hasGutter md={1}>
            <GridItem span={12}>
              <Card className="kogito-task-console--user-task_table-OverFlow">
                <DataTable
                  data={data ? data.UserTaskInstances : undefined}
                  isLoading={loading}
                  columns={columns}
                  networkStatus={networkStatus}
                  error={error}
                  refetch={refetch}
                  LoadingComponent={UserTaskLoadingComponent}
                  sortBy={sortBy}
                  onSorting={onSorting}
                />
              </Card>
            </GridItem>
          </Grid>
        </PageSection>
      </div>
    </React.Fragment>
  );
};

export default UserTaskDataTableContainer;
