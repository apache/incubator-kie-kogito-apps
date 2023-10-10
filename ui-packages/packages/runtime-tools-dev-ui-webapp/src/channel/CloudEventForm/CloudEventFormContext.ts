import React, { useContext } from 'react';
import { CloudEventFormGatewayApi } from './CloudEventFormGatewayApi';

const CloudEventFormContext =
  React.createContext<CloudEventFormGatewayApi>(null);

export const useCloudEventFormGatewayApi = (): CloudEventFormGatewayApi =>
  useContext<CloudEventFormGatewayApi>(CloudEventFormContext);

export default CloudEventFormContext;
