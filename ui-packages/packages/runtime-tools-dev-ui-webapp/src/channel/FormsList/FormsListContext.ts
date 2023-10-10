import React, { useContext } from 'react';
import { FormsListGatewayApi } from './FormsListGatewayApi';

const FormsListContext = React.createContext<FormsListGatewayApi>(null);

export const useFormsListGatewayApi = (): FormsListGatewayApi =>
  useContext<FormsListGatewayApi>(FormsListContext);

export default FormsListContext;
