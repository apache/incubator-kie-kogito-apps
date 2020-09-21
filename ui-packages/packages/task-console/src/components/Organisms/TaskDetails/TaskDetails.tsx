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
import {
  Form,
  FormGroup,
  Text,
  TextVariants,
  Tooltip
} from '@patternfly/react-core';
import React, { useContext, useState } from 'react';
import {
  componentOuiaProps,
  EndpointLink,
  GraphQL,
  KogitoEmptyState,
  KogitoEmptyStateType,
  OUIAProps
} from '@kogito-apps/common';
import TaskConsoleContext, {
  IContext
} from '../../../context/TaskConsoleContext/TaskConsoleContext';
import TaskState from '../../Atoms/TaskState/TaskState';
import { resolveTaskPriority, trimTaskEndpoint } from '../../../util/Utils';
import './TaskDetails.css';
import UserTaskInstance = GraphQL.UserTaskInstance;

interface IOwnProps {
  userTaskInstance?: UserTaskInstance;
}

const TaskDetails: React.FC<IOwnProps & OUIAProps> = ({
  userTaskInstance,
  ouiaId,
  ouiaSafe
}) => {
  const context: IContext<UserTaskInstance> = useContext(TaskConsoleContext);
  const [userTask, setUserTask] = useState<UserTaskInstance>();

  if (!userTask) {
    if (userTaskInstance) {
      setUserTask(userTaskInstance);
    } else {
      if (context.getActiveItem()) {
        setUserTask(context.getActiveItem());
      }
    }
  }

  if (!userTask) {
    return (
      <KogitoEmptyState
        type={KogitoEmptyStateType.Info}
        title="Cannot show details"
        body="Unable to show details for empty task"
      />
    );
  }

  return (
    <React.Fragment>
      <div
        {...componentOuiaProps(
          ouiaId ? ouiaId : userTask.id,
          'task-details',
          ouiaSafe
        )}
      >
        <Form>
          <FormGroup label="Name" fieldId="name">
            <Text component={TextVariants.p}>{userTask.referenceName}</Text>
          </FormGroup>
          {userTask.description && (
            <FormGroup label="Description" fieldId="description">
              <Text component={TextVariants.p}>{userTask.description}</Text>
            </FormGroup>
          )}
          <FormGroup label="ID" fieldId="id">
            <Text component={TextVariants.p}>{userTask.id}</Text>
          </FormGroup>
          <FormGroup label="State" fieldId="state">
            <TaskState task={userTask} />
          </FormGroup>
          <FormGroup label="Priority" fieldId="priority">
            <Text component={TextVariants.p}>
              {resolveTaskPriority(userTask.priority)}
            </Text>
          </FormGroup>
          <FormGroup label="Owner" fieldId="owner">
            <Text component={TextVariants.p}>
              {userTask.actualOwner || '-'}
            </Text>
          </FormGroup>
          <FormGroup label="Started" fieldId="started">
            {userTask.started ? (
              <Text component={TextVariants.p}>
                <Moment fromNow>{new Date(`${userTask.started}`)}</Moment>
              </Text>
            ) : (
              '-'
            )}
          </FormGroup>
          {userTask.completed && (
            <FormGroup label="Completed" fieldId="completed">
              <Text component={TextVariants.p}>
                <Moment fromNow>{new Date(`${userTask.completed}`)}</Moment>
              </Text>
            </FormGroup>
          )}
          <FormGroup label="Process" fieldId="process">
            <Text component={TextVariants.p}>{userTask.processId}</Text>
          </FormGroup>
          <FormGroup label="Process Instance ID" fieldId="processInstance">
            <Text component={TextVariants.p}>{userTask.processInstanceId}</Text>
          </FormGroup>
          <FormGroup label="Endpoint" fieldId="endpoint">
            <Tooltip content={userTask.endpoint}>
              <EndpointLink
                serviceUrl={userTask.endpoint}
                linkLabel={trimTaskEndpoint(userTask)}
                isLinkShown={false}
              />
            </Tooltip>
          </FormGroup>
        </Form>
      </div>
    </React.Fragment>
  );
};

export default TaskDetails;
