import React, { useMemo } from 'react';
import {
  DevUIAppContext,
  useDevUIAppContext
} from '../../components/contexts/DevUIAppContext';
import CloudEventFormContext from './CloudEventFormContext';
import { CloudEventFormGatewayApiImpl } from './CloudEventFormGatewayApi';

interface IOwnProps {
  children;
}

const CloudEventFormContextProvider: React.FC<IOwnProps> = ({ children }) => {
  const runtimeToolsApi: DevUIAppContext = useDevUIAppContext();

  const gatewayApi = useMemo(
    () => new CloudEventFormGatewayApiImpl(runtimeToolsApi.getDevUIUrl()),
    []
  );

  return (
    <CloudEventFormContext.Provider value={gatewayApi}>
      {children}
    </CloudEventFormContext.Provider>
  );
};

export default CloudEventFormContextProvider;
