/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

import {
  Card,
  Grid,
  GridItem,
  PageSection,
  InjectedOuiaProps,
  withOuiaContext,
  Bullseye,
  Label
} from '@patternfly/react-core';
import React, {useContext, useEffect, useState} from 'react';
import UserTaskPageHeader from '../../Molecules/UserTaskPageHeader/UserTaskPageHeader';
import './TaskInbox.css';
import {
  ouiaPageTypeAndObjectId,
  KogitoSpinner,
  DataTable, LoadMore
} from '@kogito-apps/common';
import {
  ICell,
  ITransform,
  IFormatterValueType
} from '@patternfly/react-table';
import TaskConsoleContext, {
  IContext
} from '../../../context/TaskConsoleContext/TaskConsoleContext';
import {
  useGetTaskForUserLazyQuery,
  UserTaskInstance
} from '../../../graphql/types';
import Moment from 'react-moment';
import TaskDescriptionColumn from '../../Atoms/TaskDescriptionColumn/TaskDescriptionColumn';
import { TaskInfo } from '../../../model/TaskInfo';

const UserTaskLoadingComponent = (
  <Bullseye>
    <KogitoSpinner spinnerText="Loading user tasks..." />
  </Bullseye>
);

const stateColumnTransformer: ITransform = (value: IFormatterValueType) => {
  if (!value) {
    return null;
  }
  const { title } = value;
  return {
    children: <Label>{title}</Label>
  };
};

const TaskInbox: React.FC<InjectedOuiaProps> = ({ ouiaContext }) => {
  const [defaultPageSize] = useState<number>(10);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [limit, setLimit] = useState<number>(defaultPageSize);
  const [queryOffset, setOffset] = useState<number>(0);
  const [pageSize, setPageSize] = useState<number>(defaultPageSize);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [tableData, setTableData] = useState<any[]>([]);

  const context: IContext<TaskInfo> = useContext(TaskConsoleContext);

  const [
    getUserTasks,
    { loading,
      error,
      data,
      refetch,
      networkStatus }
  ] = useGetTaskForUserLazyQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true,
    variables: {
      user: context.getUser().id,
      groups: context.getUser().groups,
      offset: queryOffset,
      limit: pageSize
    }
  });

  const taskNameColumnTransformer: ITransform = (
    value: IFormatterValueType,
    extra
  ) => {
    if (!value) {
      return null;
    }

    // @ts-ignore
    const task: UserTaskInstance = tableData[extra.rowIndex];

    return {
      children: <TaskDescriptionColumn task={task} />
    };
  };

  const dateColumnTransformer: ITransform = (value: IFormatterValueType) => {
    if (!value) {
      return null;
    }

    return {
      children: <Moment fromNow>{new Date(`${value.title}`)}</Moment>
    };
  };

  const columns: ICell[] = [
    {
      title: 'Name',
      data: 'name',
      cellTransforms: [taskNameColumnTransformer]
    },
    {
      title: 'ProcessId',
      data: 'processId'
    },
    {
      title: 'Priority',
      data: 'priority'
    },
    {
      title: 'State',
      data: 'state',
      cellTransforms: [stateColumnTransformer]
    },
    {
      title: 'Started',
      data: 'started',
      cellTransforms: [dateColumnTransformer]
    },
    {
      title: 'Last update',
      data: 'lastUpdate',
      cellTransforms: [dateColumnTransformer]
    }
  ];

  const onGetMoreInstances = (_queryOffset, _pageSize) => {
    setIsLoadingMore(true);

    if(_queryOffset !== queryOffset) {
      setOffset(_queryOffset)
    }

    if(_pageSize !== pageSize) {
      setPageSize(_pageSize);
    }

    getUserTasks({
      variables: {
        user: context.getUser().id,
        groups: context.getUser().groups,
        offset: _queryOffset,
        limit: _pageSize
      }
    });
  };

  useEffect(() => {
    return ouiaPageTypeAndObjectId(ouiaContext, 'task-inbox');
  });

  useEffect(() => {
    onGetMoreInstances(queryOffset, pageSize);
  }, [])

  useEffect(() => {
    if (isLoadingMore === undefined || !isLoadingMore) {
      setIsLoading(loading);
    }
    if (!loading && data !== undefined) {
      const newData = tableData.concat(data.UserTaskInstances)
      setTableData(newData);
      setLimit(tableData.length);
      if (queryOffset > 0 && tableData.length > 0) {
        setIsLoadingMore(false);
      }
    }
  }, [data]);
  return (
    <React.Fragment>
      <UserTaskPageHeader />
      <PageSection>
        <Grid gutter="md">
          <GridItem span={12}>
            <Card className="kogito-task-console-task-inbox_table-OverFlow">
              <DataTable
                data={tableData}
                isLoading={loading}
                columns={columns}
                networkStatus={networkStatus}
                error={error}
                refetch={refetch}
                LoadingComponent={UserTaskLoadingComponent}
              />

              {(!loading || isLoadingMore) &&
              !isLoading &&
                  tableData.length > 0 &&
              (limit === pageSize || isLoadingMore) && (
                  <LoadMore
                      offset={queryOffset}
                      setOffset={setOffset}
                      getMoreItems={onGetMoreInstances}
                      pageSize={pageSize}
                      isLoadingMore={false}
                  />
              )}
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default withOuiaContext(TaskInbox);
