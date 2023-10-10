import React, { useContext } from 'react';
import { ProcessDefinitionListGatewayApi } from './ProcessDefinitionListGatewayApi';

const ProcessDefinitionListContext =
  React.createContext<ProcessDefinitionListGatewayApi>(null);

export const useProcessDefinitionListGatewayApi =
  (): ProcessDefinitionListGatewayApi =>
    useContext<ProcessDefinitionListGatewayApi>(ProcessDefinitionListContext);

export default ProcessDefinitionListContext;
