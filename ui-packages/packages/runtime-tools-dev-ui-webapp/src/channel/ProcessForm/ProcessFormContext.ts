import React, { useContext } from 'react';
import { ProcessFormGatewayApi } from './ProcessFormGatewayApi';

const ProcessFormContext = React.createContext<ProcessFormGatewayApi>(null);

export const useProcessFormGatewayApi = (): ProcessFormGatewayApi =>
  useContext<ProcessFormGatewayApi>(ProcessFormContext);

export default ProcessFormContext;
