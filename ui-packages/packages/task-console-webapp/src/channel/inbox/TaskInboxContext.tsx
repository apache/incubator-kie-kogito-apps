import React, { useContext } from 'react';
import { TaskInboxGatewayApi } from './TaskInboxGatewayApi';

const TaskInboxContext = React.createContext<TaskInboxGatewayApi>(null);

export const useTaskInboxGatewayApi = (): TaskInboxGatewayApi =>
  useContext<TaskInboxGatewayApi>(TaskInboxContext);

export default TaskInboxContext;
