import React from 'react';
import { useKogitoAppContext } from '@kogito-apps/consoles-common/dist/environment/context';
import { TaskFormGatewayApiImpl } from './TaskFormGatewayApi';
import TaskFormContext from './TaskFormContext';

const TaskFormContextProvider: React.FC = ({ children }) => {
  const appContext = useKogitoAppContext();

  return (
    <TaskFormContext.Provider
      value={new TaskFormGatewayApiImpl(appContext.getCurrentUser())}
    >
      {children}
    </TaskFormContext.Provider>
  );
};

export default TaskFormContextProvider;
