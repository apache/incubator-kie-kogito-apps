import React from 'react';
import { ApolloClient } from 'apollo-client';
import { useDevUIAppContext } from '../../components/contexts/DevUIAppContext';
import TaskInboxContext from './TaskInboxContext';
import { TaskInboxGatewayApiImpl } from './TaskInboxGatewayApi';
import { GraphQLTaskInboxQueries } from './TaskInboxQueries';

interface IOwnProps {
  apolloClient: ApolloClient<any>;
  children;
}

const TaskInboxContextProvider: React.FC<IOwnProps> = ({
  apolloClient,
  children
}) => {
  const appContext = useDevUIAppContext();

  return (
    <TaskInboxContext.Provider
      value={
        new TaskInboxGatewayApiImpl(
          new GraphQLTaskInboxQueries(apolloClient),
          () => appContext.getCurrentUser()
        )
      }
    >
      {children}
    </TaskInboxContext.Provider>
  );
};

export default TaskInboxContextProvider;
