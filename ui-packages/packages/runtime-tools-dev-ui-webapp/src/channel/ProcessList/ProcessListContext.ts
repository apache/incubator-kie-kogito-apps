import React, { useContext } from 'react';
import { ProcessListGatewayApi } from './ProcessListGatewayApi';

const ProcessListContext = React.createContext<ProcessListGatewayApi>(null);

export const useProcessListGatewayApi = (): ProcessListGatewayApi =>
  useContext<ProcessListGatewayApi>(ProcessListContext);

export default ProcessListContext;
