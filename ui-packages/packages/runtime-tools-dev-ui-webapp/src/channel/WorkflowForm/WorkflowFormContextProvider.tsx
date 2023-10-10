import React from 'react';
import WorkflowFormContext from './WorkflowFormContext';
import { WorkflowFormGatewayApiImpl } from './WorkflowFormGatewayApi';
import {
  DevUIAppContext,
  useDevUIAppContext
} from '../../components/contexts/DevUIAppContext';

interface IOwnProps {
  children;
}

const WorkflowFormContextProvider: React.FC<IOwnProps> = ({ children }) => {
  const runtimeToolsApi: DevUIAppContext = useDevUIAppContext();
  return (
    <WorkflowFormContext.Provider
      value={
        new WorkflowFormGatewayApiImpl(
          runtimeToolsApi.getDevUIUrl(),
          runtimeToolsApi.getOpenApiPath()
        )
      }
    >
      {children}
    </WorkflowFormContext.Provider>
  );
};

export default WorkflowFormContextProvider;
