import React from 'react';
import { useDevUIAppContext } from '../../components/contexts/DevUIAppContext';
import { TaskFormGatewayApiImpl } from './TaskFormGatewayApi';
import TaskFormContext from './TaskFormContext';

const TaskFormContextProvider: React.FC = ({ children }) => {
  const appContext = useDevUIAppContext();

  return (
    <TaskFormContext.Provider
      value={new TaskFormGatewayApiImpl(() => appContext.getCurrentUser())}
    >
      {children}
    </TaskFormContext.Provider>
  );
};

export default TaskFormContextProvider;
