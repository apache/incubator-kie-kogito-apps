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

import React, { useContext, useEffect, useState } from 'react';
import { Bullseye } from '@patternfly/react-core';
import Moment from 'react-moment';
import {
  KogitoSpinner,
  DataTable,
  LoadMore,
  GraphQL,
  DataTableColumn,
  ServerErrors,
  KogitoEmptyStateType,
  KogitoEmptyState
} from '@kogito-apps/common';
import TaskConsoleContext, {
  IContext
} from '../../../context/TaskConsoleContext/TaskConsoleContext';
import TaskDescriptionColumn from '../../Atoms/TaskDescriptionColumn/TaskDescriptionColumn';
import UserTaskInstance = GraphQL.UserTaskInstance;
import TaskStateColumn from '../../Atoms/TaskStateColumn/TaskStateColumn';

const UserTaskLoadingComponent = (
  <Bullseye>
    <KogitoSpinner spinnerText="Loading user tasks..." />
  </Bullseye>
);

const TaskInbox: React.FC = props => {
  const [defaultPageSize] = useState<number>(10);
  const [isLoaded, setIsLoaded] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [queryOffset, setOffset] = useState<number>(0);
  const [pageSize, setPageSize] = useState<number>(defaultPageSize);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [tableData, setTableData] = useState<any[]>([]);

  const context: IContext<UserTaskInstance> = useContext(TaskConsoleContext);

  const [
    getUserTasks,
    { loading, error, data, refetch, networkStatus }
  ] = GraphQL.useGetTaskForUserLazyQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true,
    variables: {
      user: context.getUser().id,
      groups: context.getUser().groups,
      offset: queryOffset,
      limit: pageSize
    }
  });

  const onGetMoreInstances = (_queryOffset, _pageSize, _loadMore) => {
    setIsLoadingMore(_loadMore);

    if (_queryOffset !== queryOffset) {
      setOffset(_queryOffset);
    }

    if (_pageSize !== pageSize) {
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
    onGetMoreInstances(queryOffset, pageSize, false);
  }, []);

  useEffect(() => {
    if (isLoadingMore === undefined || !isLoadingMore) {
      setIsLoading(loading);
    }
    if (!loading && data !== undefined) {
      const newData = tableData.concat(data.UserTaskInstances);
      setTableData(newData);
      if (queryOffset > 0 && tableData.length > 0) {
        setIsLoadingMore(false);
      }
      if (!isLoaded) {
        setIsLoaded(true);
      }
    }
  }, [data]);

  if (error) {
    return <ServerErrors error={error} variant={'large'} />;
  }

  if (!isLoaded) {
    return UserTaskLoadingComponent;
  }

  if (tableData.length === 0) {
    return (
      <KogitoEmptyState
        type={KogitoEmptyStateType.Search}
        title="No results found"
        body="Try using different filters"
      />
    );
  }

  const columns: DataTableColumn[] = [
    {
      label: 'Name',
      path: 'referenceName',
      bodyCellTransformer: (cellValue, rowTask) => (
        <TaskDescriptionColumn task={rowTask} />
      )
    },
    {
      label: 'Process',
      path: 'processId'
    },
    {
      label: 'Priority',
      path: 'priority'
    },
    {
      label: 'State',
      path: 'state',
      bodyCellTransformer: (cellValue, rowTask) => (
        <TaskStateColumn task={rowTask} />
      )
    },
    {
      label: 'Started',
      path: 'started',
      bodyCellTransformer: started => (
        <Moment fromNow>{new Date(`${started}`)}</Moment>
      )
    },
    {
      label: 'Last update',
      path: 'lastUpdate',
      bodyCellTransformer: lastUpdate => (
        <Moment fromNow>{new Date(`${lastUpdate}`)}</Moment>
      )
    }
  ];

  const mustShowMore =
    isLoadingMore || (data && data.UserTaskInstances.length === pageSize);

  return (
    <React.Fragment>
      <DataTable
        data={tableData}
        isLoading={isLoading}
        columns={columns}
        networkStatus={networkStatus}
        error={error}
        refetch={refetch}
        LoadingComponent={UserTaskLoadingComponent}
      />
      {mustShowMore && (
        <LoadMore
          offset={queryOffset}
          setOffset={setOffset}
          getMoreItems={(_initval, _pageSize) =>
            onGetMoreInstances(_initval, _pageSize, true)
          }
          pageSize={pageSize}
          isLoadingMore={isLoadingMore}
        />
      )}
    </React.Fragment>
  );
};

export default TaskInbox;
