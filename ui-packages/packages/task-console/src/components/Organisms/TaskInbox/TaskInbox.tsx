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
import React, { useContext, useEffect } from 'react';
import UserTaskPageHeader from '../../Molecules/UserTaskPageHeader/UserTaskPageHeader';
import './TaskInbox.css';
import {
  ouiaPageTypeAndObjectId,
  KogitoSpinner,
  DataTable
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
  useGetTaskForUserQuery,
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
  const context: IContext<TaskInfo> = useContext(TaskConsoleContext);

  const {
    loading,
    error,
    data,
    refetch,
    networkStatus
  } = useGetTaskForUserQuery({
    variables: {
      user: context.getUser().id,
      groups: context.getUser().groups
    },
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true
  });

  const taskNameColumnTransformer: ITransform = (
    value: IFormatterValueType,
    extra
  ) => {
    if (!value) {
      return null;
    }

    // @ts-ignore
    const task: UserTaskInstance = data.UserTaskInstances[extra.rowIndex];

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

  useEffect(() => {
    return ouiaPageTypeAndObjectId(ouiaContext, 'task-inbox');
  });

  return (
    <React.Fragment>
      <UserTaskPageHeader />
      <PageSection>
        <Grid gutter="md">
          <GridItem span={12}>
            <Card className="kogito-task-console-task-inbox_table-OverFlow">
              <DataTable
                data={data ? data.UserTaskInstances : undefined}
                isLoading={loading}
                columns={columns}
                networkStatus={networkStatus}
                error={error}
                refetch={refetch}
                LoadingComponent={UserTaskLoadingComponent}
              />
            </Card>
          </GridItem>
        </Grid>
      </PageSection>
    </React.Fragment>
  );
};

export default withOuiaContext(TaskInbox);
