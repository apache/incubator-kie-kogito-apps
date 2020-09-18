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
import React from 'react';
import TaskConsoleContext, { DefaultContext } from './TaskConsoleContext';
import { GraphQL, User } from '@kogito-apps/common';
import UserTaskInstance = GraphQL.UserTaskInstance;

interface IOwnProps {
  user: User;
  children;
}

const TaskConsoleContextProvider: React.FC<IOwnProps> = ({
  user,
  children
}) => {
  return (
    <TaskConsoleContext.Provider
      value={new DefaultContext<UserTaskInstance>(user)}
    >
      {children}
    </TaskConsoleContext.Provider>
  );
};

export default TaskConsoleContextProvider;
