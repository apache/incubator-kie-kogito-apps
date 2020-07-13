import React from 'react';
import TaskConsoleContext, { DefaultContext } from './TaskConsoleContext';
import { TaskInfo } from '../../model/TaskInfo';
import User from '@kogito-apps/common/src/models/User/User';

interface IOwnProps {
  user: User;
}

const TaskConsoleContextProvider: React.FC<IOwnProps> = ({
  user,
  children
}) => {
  return (
    <TaskConsoleContext.Provider value={new DefaultContext<TaskInfo>(user)}>
      {children}
    </TaskConsoleContext.Provider>
  );
};

export default TaskConsoleContextProvider;
