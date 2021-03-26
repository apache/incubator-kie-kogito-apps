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

import React from 'react';
import { ApolloClient } from 'apollo-client';
import { useKogitoAppContext } from '@kogito-apps/consoles-common';
import TaskInboxContext from '../../channel/inbox/TaskInboxContext';
import { TaskInboxGatewayApiImpl } from '../../channel/inbox/TaskInboxGatewayApi';
import { GraphQLTaskInboxQueries } from '../../channel/inbox/TaskInboxQueries';

interface IOwnProps {
  apolloClient: ApolloClient<any>;
  children;
}

const TaskInboxContextProvider: React.FC<IOwnProps> = ({
  apolloClient,
  children
}) => {
  const appContext = useKogitoAppContext();

  return (
    <TaskInboxContext.Provider
      value={
        new TaskInboxGatewayApiImpl(
          appContext.getCurrentUser(),
          new GraphQLTaskInboxQueries(apolloClient),
          task => window.alert(`Task opened: ${task.id}`)
        )
      }
    >
      {children}
    </TaskInboxContext.Provider>
  );
};

export default TaskInboxContextProvider;
