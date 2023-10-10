import React from 'react';
import { ApolloClient } from 'apollo-client';
import { useKogitoAppContext } from '@kogito-apps/consoles-common/dist/environment/context';
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
          new GraphQLTaskInboxQueries(apolloClient)
        )
      }
    >
      {children}
    </TaskInboxContext.Provider>
  );
};

export default TaskInboxContextProvider;
