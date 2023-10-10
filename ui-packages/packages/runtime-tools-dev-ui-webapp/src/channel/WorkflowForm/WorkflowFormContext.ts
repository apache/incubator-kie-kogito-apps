import React, { useContext } from 'react';
import { WorkflowFormGatewayApi } from './WorkflowFormGatewayApi';

const WorkflowFormContext = React.createContext<WorkflowFormGatewayApi>(null);

export const useWorkflowFormGatewayApi = (): WorkflowFormGatewayApi =>
  useContext<WorkflowFormGatewayApi>(WorkflowFormContext);

export default WorkflowFormContext;
