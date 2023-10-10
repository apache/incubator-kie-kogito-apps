import React, { useMemo } from 'react';
import {
  DevUIAppContext,
  useDevUIAppContext
} from '../../components/contexts/DevUIAppContext';
import ProcessDefinitionListContext from './ProcessDefinitionListContext';
import { ProcessDefinitionListGatewayApiImpl } from './ProcessDefinitionListGatewayApi';

interface ProcessDefinitionListContextProviderProps {
  children;
}

const ProcessDefinitionListContextProvider: React.FC<
  ProcessDefinitionListContextProviderProps
> = ({ children }) => {
  const runtimeToolsApi: DevUIAppContext = useDevUIAppContext();

  const gatewayApiImpl = useMemo(() => {
    return new ProcessDefinitionListGatewayApiImpl(
      runtimeToolsApi.getDevUIUrl(),
      runtimeToolsApi.getOpenApiPath()
    );
  }, []);

  return (
    <ProcessDefinitionListContext.Provider value={gatewayApiImpl}>
      {children}
    </ProcessDefinitionListContext.Provider>
  );
};

export default ProcessDefinitionListContextProvider;
