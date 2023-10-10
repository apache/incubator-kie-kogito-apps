import React from 'react';
import { ApolloClient } from 'apollo-client';
import { TaskInboxContextProvider } from '../../channel/inbox';
import TaskFormContextProvider from '../../channel/forms/TaskFormContextProvider';

interface IOwnProps {
  client: ApolloClient<any>;
  children;
}

const TaskConsoleContextsProvider: React.FC<IOwnProps> = ({
  client,
  children
}) => {
  return (
    <TaskInboxContextProvider apolloClient={client}>
      <TaskFormContextProvider>{children}</TaskFormContextProvider>
    </TaskInboxContextProvider>
  );
};

export default TaskConsoleContextsProvider;
