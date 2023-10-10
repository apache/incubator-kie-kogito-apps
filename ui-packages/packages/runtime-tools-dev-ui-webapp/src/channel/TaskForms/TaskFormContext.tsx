import React, { useContext } from 'react';
import { TaskFormGatewayApi } from './TaskFormGatewayApi';

const TaskFormContext = React.createContext<TaskFormGatewayApi>(null);

export const useTaskFormGatewayApi = (): TaskFormGatewayApi =>
  useContext<TaskFormGatewayApi>(TaskFormContext);

export default TaskFormContext;
