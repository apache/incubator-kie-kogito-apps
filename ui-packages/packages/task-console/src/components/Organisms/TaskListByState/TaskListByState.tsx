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
import React, { useEffect, useState } from 'react';
import { DataList, Bullseye } from '@patternfly/react-core';
import TaskListItem from '../../Molecules/TaskListItem/TaskListItem';
import {
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  GraphQL
} from '@kogito-apps/common';
import '@patternfly/patternfly/patternfly-addons.css';

interface IOwnProps {
  currentState: string;
}

const TaskListByState: React.FC<IOwnProps> = ({ currentState }) => {
  const {
    loading,
    error,
    data,
    networkStatus
  } = GraphQL.useGetUserTasksByStatesQuery({
    variables: {
      state: [currentState]
    },
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true
  });
  const [childList, setChildList] = useState<any>([]);

  useEffect(() => {
    setChildList(data);
  }, [data]);

  if (loading) {
    return (
      <Bullseye>
        <KogitoSpinner spinnerText="Loading user tasks..." />
      </Bullseye>
    );
  }

  if (networkStatus === 4) {
    return (
      <Bullseye>
        <KogitoSpinner spinnerText="Loading user tasks..." />
      </Bullseye>
    );
  }

  if (error) {
    return (
      <div className=".pf-u-my-xl">
        <KogitoEmptyState
          type={KogitoEmptyStateType.Refresh}
          title="Oops... error while loading"
          body="Try using the refresh action to reload user tasks"
        />
      </div>
    );
  }

  return (
    <DataList aria-label="User Task list">
      {!loading &&
        childList !== undefined &&
        childList.UserTaskInstances.map((item, index) => {
          return (
            <TaskListItem
              id={index}
              key={item.id}
              userTaskInstanceData={item}
            />
          );
        })}
      {loading && (
        <Bullseye>
          <KogitoSpinner spinnerText="Loading user tasks..." />
        </Bullseye>
      )}
    </DataList>
  );
};

export default TaskListByState;
