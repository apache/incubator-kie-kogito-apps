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
import Moment from 'react-moment';
import React, { useContext } from 'react';
import {
  DataListAction,
  DataListCell,
  DataListItem,
  DataListItemCells,
  DataListItemRow
} from '@patternfly/react-core';
import { Link } from 'react-router-dom';
import TaskConsoleContext, {
  IContext
} from '../../../context/TaskConsoleContext/TaskConsoleContext';
import { GraphQL } from '@kogito-apps/common';
import UserTaskInstance = GraphQL.UserTaskInstance;

export interface IOwnProps {
  id: number;
  userTaskInstanceData: UserTaskInstance;
}

const TaskListItem: React.FC<IOwnProps> = ({ userTaskInstanceData }) => {
  const context: IContext<UserTaskInstance> = useContext(TaskConsoleContext);

  return (
    <React.Fragment>
      <DataListItem aria-labelledby="kie-datalist-item">
        <DataListItemRow>
          <DataListItemCells
            dataListCells={[
              <DataListCell key={1}>{userTaskInstanceData.name}</DataListCell>,
              <DataListCell key={2}>
                {userTaskInstanceData.started ? (
                  <Moment fromNow>
                    {new Date(`${userTaskInstanceData.started}`)}
                  </Moment>
                ) : (
                  ''
                )}
              </DataListCell>,
              <DataListCell key={3}>
                {userTaskInstanceData.processId}
              </DataListCell>,
              <DataListCell key={4}>
                {userTaskInstanceData.processInstanceId}
              </DataListCell>,
              <DataListCell key={5}>{userTaskInstanceData.state}</DataListCell>
            ]}
          />

          <DataListAction
            aria-labelledby="kie-datalist-item kie-datalist-action"
            id="kie-datalist-action"
            aria-label="Actions"
          >
            <Link
              to={'/Task/' + userTaskInstanceData.id}
              onClick={() => {
                context.setActiveItem(userTaskInstanceData);
              }}
            >
              Open Task
            </Link>
          </DataListAction>
        </DataListItemRow>
      </DataListItem>
    </React.Fragment>
  );
};

export default TaskListItem;
