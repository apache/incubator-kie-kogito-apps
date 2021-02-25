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

import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import { Bullseye } from '@patternfly/react-core';
import {
  DataTable,
  DataTableColumn,
  LoadMore,
  OUIAProps,
  componentOuiaProps,
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  ServerErrors
} from '@kogito-apps/components-common';
import {
  QueryFilter,
  SortBy,
  TaskInboxDriver,
  TaskInboxState
} from '../../../api';
import { UserTaskInstance } from '../../../types';
import TaskInboxToolbar from '../TaskInboxToolbar/TaskInboxToolbar';
import {
  getDateColumn,
  getDefaultActiveTaskStates,
  getDefaultColumn,
  getDefaultTaskStates,
  getTaskDescriptionColumn,
  getTaskStateColumn
} from '../utils/Utils';

export interface TaskInboxProps {
  isEnvelopeConnectedToChannel: boolean;
  initialState?: TaskInboxState;
  driver: TaskInboxDriver;
  allTaskStates?: string[];
  activeTaskStates?: string[];
}

const UserTaskLoadingComponent = (
  <Bullseye>
    <KogitoSpinner
      spinnerText="Loading user tasks..."
      ouiaId="task-inbox-loading-tasks"
    />
  </Bullseye>
);

const TaskInbox: React.FC<TaskInboxProps & OUIAProps> = ({
  isEnvelopeConnectedToChannel,
  initialState,
  driver,
  allTaskStates,
  activeTaskStates,
  ouiaId,
  ouiaSafe
}) => {
  const [allStates] = useState<string[]>(
    allTaskStates || getDefaultTaskStates()
  );
  const [activeStates] = useState<string[]>(
    activeTaskStates || getDefaultActiveTaskStates()
  );
  const [sortBy, setSortBy] = useState<SortBy>({
    property: 'lastUpdate',
    direction: 'desc'
  });
  const [pageSize] = useState<number>(10);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [offset, setOffset] = useState<number>(0);
  const [error, setError] = useState<any>(undefined);
  const [showEmptyFiltersError, setShowEmptyFiltersError] = useState<boolean>(
    false
  );

  const [tasks, setTasks] = useState<UserTaskInstance[]>([]);

  const [columns] = useState<DataTableColumn[]>([
    getTaskDescriptionColumn((task: UserTaskInstance): void =>
      driver.openTask(task)
    ),
    getDefaultColumn('processId', 'Process', true),
    getDefaultColumn('priority', 'Priority', true),
    getTaskStateColumn(),
    getDateColumn('started', 'Started'),
    getDateColumn('lastUpdate', 'Last update')
  ]);

  const getTableSortBy = () => {
    return {
      index: columns.findIndex(column => column.path === sortBy.property),
      direction: sortBy.direction
    };
  };

  const initDefault = async () => {
    const defaultState: TaskInboxState = {
      filters: {
        taskStates: [...activeStates],
        taskNames: []
      },
      sortBy,
      currentPage: { offset: 0, limit: 10 }
    };
    await driver.setInitialState(defaultState);
    setIsLoading(true);
    setSortBy(defaultState.sortBy);
    doQueryTasks(0, pageSize, true);
  };

  const doQueryTasks = async (
    _offset: number,
    _limit: number,
    _resetTasks: boolean,
    _resetPagination: boolean = false,
    _loadMore: boolean = false
  ) => {
    setIsLoadingMore(_loadMore);
    setError(undefined);

    try {
      const response: UserTaskInstance[] = await driver.query(_offset, _limit);
      if (_resetTasks) {
        setTasks(response);
      } else {
        setTasks(tasks.concat(response));
      }

      if (_resetPagination) {
        setOffset(_offset);
      }
    } catch (err) {
      setError(err);
    } finally {
      setIsLoading(false);
      setIsLoadingMore(false);
    }
  };

  const doApplyFilter = async (filter: QueryFilter) => {
    if (
      !filter ||
      (_.isEmpty(filter.taskStates) && _.isEmpty(filter.taskNames))
    ) {
      setShowEmptyFiltersError(true);
      return;
    }
    setShowEmptyFiltersError(false);
    setIsLoading(true);
    await driver.applyFilter(filter);
    doQueryTasks(0, pageSize, true, true);
  };

  const doRefresh = async () => {
    setIsLoading(true);
    setError(undefined);
    setIsLoadingMore(false);
    try {
      const response: UserTaskInstance[] = await driver.refresh();
      setTasks(response);
    } catch (err) {
      setError(err);
    } finally {
      setIsLoading(false);
    }
  };

  const onSort = async (index: number, direction) => {
    const sortObj: SortBy = {
      property: columns[index].path,
      direction: direction.toLowerCase()
    };
    await driver.applySorting(sortObj);
    setSortBy(sortObj);
    await doRefresh();
  };

  useEffect(() => {
    if (!isEnvelopeConnectedToChannel) {
      setIsLoading(true);
    } else {
      if (!initialState) {
        initDefault();
      }
    }
  }, [isEnvelopeConnectedToChannel]);

  if (error) {
    return <ServerErrors error={error} variant={'large'} />;
  }

  const mustShowMore: boolean =
    isLoadingMore || (offset + pageSize === tasks.length && !isLoading);

  return (
    <div {...componentOuiaProps(ouiaId, 'task-inbox', ouiaSafe)}>
      <TaskInboxToolbar
        initialState={initialState ? initialState.filters : undefined}
        allTaskStates={allStates}
        activeTaskStates={activeStates}
        applyFilter={doApplyFilter}
        refresh={doRefresh}
      />
      {showEmptyFiltersError ? (
        <KogitoEmptyState
          type={KogitoEmptyStateType.Reset}
          title="No status is selected"
          body="Try selecting at least one status to see results"
          onClick={() =>
            doApplyFilter({ taskStates: activeStates, taskNames: [] })
          }
          ouiaId="task-inbox-no-status"
        />
      ) : (
        <>
          <DataTable
            data={tasks}
            isLoading={isLoading}
            columns={columns}
            error={false}
            sortBy={getTableSortBy()}
            onSorting={onSort}
            LoadingComponent={UserTaskLoadingComponent}
          />
          {mustShowMore && (
            <LoadMore
              offset={offset}
              setOffset={setOffset}
              getMoreItems={(_offset, _limit) =>
                doQueryTasks(_offset, _limit, false, true, true)
              }
              pageSize={pageSize}
              isLoadingMore={isLoadingMore}
            />
          )}
        </>
      )}
    </div>
  );
};

export default TaskInbox;
