import React, { useContext } from 'react';
import { ProcessDetailsGatewayApi } from './ProcessDetailsGatewayApi';

const ProcessDetailsContext =
  React.createContext<ProcessDetailsGatewayApi>(null);

export const useProcessDetailsGatewayApi = (): ProcessDetailsGatewayApi =>
  useContext<ProcessDetailsGatewayApi>(ProcessDetailsContext);

export default ProcessDetailsContext;
